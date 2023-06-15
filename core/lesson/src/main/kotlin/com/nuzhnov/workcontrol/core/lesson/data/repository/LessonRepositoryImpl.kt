package com.nuzhnov.workcontrol.core.lesson.data.repository

import com.nuzhnov.workcontrol.core.lesson.data.datasource.LessonRemoteDataSource
import com.nuzhnov.workcontrol.core.lesson.data.datasource.LessonLocalDataSource
import com.nuzhnov.workcontrol.core.lesson.data.datasource.ParticipantRemoteDataSource
import com.nuzhnov.workcontrol.core.lesson.data.datasource.ParticipantLocalDataSource
import com.nuzhnov.workcontrol.core.lesson.domen.repository.LessonRepository
import com.nuzhnov.workcontrol.core.lesson.domen.exception.LessonException
import com.nuzhnov.workcontrol.core.data.api.dto.lesson.LessonDTO
import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.data.database.entity.model.LessonModel
import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.data.preferences.model.Session
import com.nuzhnov.workcontrol.core.data.mapper.*
import com.nuzhnov.workcontrol.core.models.Lesson
import com.nuzhnov.workcontrol.core.models.Discipline
import com.nuzhnov.workcontrol.core.models.util.LoadResult
import com.nuzhnov.workcontrol.core.util.roles.requireTeacherRole
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LessonRepositoryImpl @Inject constructor(
    private val lessonRemoteDataSource: LessonRemoteDataSource,
    private val lessonLocalDataSource: LessonLocalDataSource,
    private val participantRemoteDataSource: ParticipantRemoteDataSource,
    private val participantLocalDataSource: ParticipantLocalDataSource,
    private val appPreferences: AppPreferences,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : LessonRepository {

    override fun getScheduledLessonsFlow() = flow {
        val flow = appPreferences.requireTeacherRole { session ->
            lessonLocalDataSource.getTeacherScheduledLessonsFlow(teacherID = session.id)
        }.map { modelList -> modelList.map(LessonModel::toLesson) }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override fun getActiveLessonFlow() = flow {
        val flow = appPreferences.requireTeacherRole { session ->
            lessonLocalDataSource.getTeacherActiveLessonFlow(teacherID = session.id)
        }.map { model -> model?.toLesson() }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override fun getFinishedLessonsFlow() = flow {
        val flow = appPreferences.requireTeacherRole { session ->
            lessonLocalDataSource.getTeacherFinishedLessonsFlow(teacherID = session.id)
        }.map { modelList -> modelList.map(LessonModel::toLesson) }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override fun getDisciplineScheduledLessonsFlow(discipline: Discipline) = flow {
        val flow = appPreferences.requireTeacherRole { session ->
            lessonLocalDataSource.getTeacherDisciplineScheduledLessonsFlow(
                teacherID = session.id,
                disciplineID = discipline.id
            )
        }.map { modelList -> modelList.map(LessonModel::toLesson) }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override fun getDisciplineFinishedLessonsFlow(discipline: Discipline) = flow {
        val flow = appPreferences.requireTeacherRole { session ->
            lessonLocalDataSource.getTeacherDisciplineFinishedLessonsFlow(
                teacherID = session.id,
                disciplineID = discipline.id
            )
        }.map { modelList -> modelList.map(LessonModel::toLesson) }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override suspend fun refreshFinishedLessons() = refresh {
        val response = lessonRemoteDataSource.getFinishedLessons()

        if (response is Response.Success) {
            val lessonModels = response.value
                .map(LessonDTO::toLessonModel)
                .toTypedArray()

            lessonLocalDataSource.saveLessonModels(*lessonModels)
        }

        response.toLoadResult()
    }

    override suspend fun refreshDisciplineFinishedLessons(discipline: Discipline) = refresh {
        val response = lessonRemoteDataSource.getDisciplineFinishedLessons(discipline.id)

        if (response is Response.Success) {
            val lessonModels = response.value
                .map(LessonDTO::toLessonModel)
                .toTypedArray()

            lessonLocalDataSource.saveLessonModels(*lessonModels)
        }

        response.toLoadResult()
    }

    override suspend fun addLesson(lesson: Lesson) = executeOperation {
        val groupIDList = lesson.associatedGroups.map { group -> group.id }
        val response = participantRemoteDataSource.getStudentsOfGroups(groupIDList)

        if (response is Response.Success) {
            val students = response.value.map { (groupID, studentDTO) ->
                studentDTO.toStudentEntity(groupID)
            }

            lessonLocalDataSource.addLesson(lessonModel = lesson.toLessonModel())
            participantLocalDataSource.attachStudentsToLesson(lesson.id, students)
        }
    }

    override suspend fun updateLesson(lesson: Lesson) = executeOperation {
        val groupIDList = lesson.associatedGroups.map { group -> group.id }
        val response = participantRemoteDataSource.getStudentsOfGroups(groupIDList)

        if (response is Response.Success) {
            val students = response.value.map { (groupID, studentDTO) ->
                studentDTO.toStudentEntity(groupID)
            }

            lessonLocalDataSource.updateLesson(lessonModel = lesson.toLessonModel())
            participantLocalDataSource.attachStudentsToLesson(lesson.id, students)
        }
    }

    override suspend fun removeLesson(lesson: Lesson) = executeOperation {
        lessonLocalDataSource.removeLesson(lessonEntity = lesson.toLessonEntity())

        if (lesson.state == Lesson.State.ACTIVE) {
            // TODO: Cancel notification and finalizer jobs
        }
    }

    override suspend fun startLesson(lesson: Lesson) = executeOperation { session ->
        lessonLocalDataSource.startLesson(lessonID = lesson.id, teacherID = session.id)

        // TODO: Schedule notification and finalizer jobs
    }

    override suspend fun finishLesson(lesson: Lesson) = executeOperation {
        lessonLocalDataSource.finishLesson(lessonID = lesson.id)

        if (lesson.state == Lesson.State.ACTIVE) {
            // TODO: Cancel notification and finilizer jobs
        }
    }

    private suspend fun refresh(block: suspend (Session) -> LoadResult) =
        withContext(context = coroutineDispatcher) {
            val session = appPreferences.requireTeacherRole()

            safeExecute { block(session) }
                .onFailure { cause ->
                    if (cause is LessonException) {
                        throw cause
                    }
                }.unwrap()
        }

    private suspend fun executeOperation(block: suspend (Session) -> Unit) =
        withContext(context = coroutineDispatcher) {
            val session = appPreferences.requireTeacherRole()

            safeExecute { block(session) }
                .onFailure { cause ->
                    if (cause is LessonException) {
                        throw cause
                    }
                }.toOperationResult()
        }
}
