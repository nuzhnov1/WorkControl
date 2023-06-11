package com.nuzhnov.workcontrol.core.lesson.data.repository

import com.nuzhnov.workcontrol.core.lesson.data.datasource.LessonRemoteDataSource
import com.nuzhnov.workcontrol.core.lesson.data.datasource.LessonLocalDataSource
import com.nuzhnov.workcontrol.core.lesson.data.datasource.ParticipantRemoteDataSource
import com.nuzhnov.workcontrol.core.lesson.data.datasource.ParticipantLocalDataSource
import com.nuzhnov.workcontrol.core.lesson.domen.repository.LessonRepository
import com.nuzhnov.workcontrol.core.data.api.dto.lesson.LessonDTO
import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.data.database.entity.model.LessonModel
import com.nuzhnov.workcontrol.core.data.mapper.*
import com.nuzhnov.workcontrol.core.model.Lesson
import com.nuzhnov.workcontrol.core.model.Discipline
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LessonRepositoryImpl @Inject constructor(
    private val lessonRemoteDataSource: LessonRemoteDataSource,
    private val lessonLocalDataSource: LessonLocalDataSource,
    private val participantRemoteDataSource: ParticipantRemoteDataSource,
    private val participantLocalDataSource: ParticipantLocalDataSource,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : LessonRepository {

    override fun getScheduledLessonsFlow(): Flow<List<Lesson>> = lessonLocalDataSource
        .getTeacherScheduledLessonsFlow()
        .map { lessonModelList -> lessonModelList.map(LessonModel::toLesson) }
        .flowOn(context = coroutineDispatcher)

    override fun getActiveLessonFlow(): Flow<Lesson?> = lessonLocalDataSource
        .getTeacherActiveLessonFlow()
        .map { lessonModel -> lessonModel?.toLesson() }
        .flowOn(context = coroutineDispatcher)

    override fun getFinishedLessonsFlow(): Flow<List<Lesson>> = lessonLocalDataSource
        .getTeacherFinishedLessonsFlow()
        .map { lessonModelList -> lessonModelList.map(LessonModel::toLesson) }
        .flowOn(context = coroutineDispatcher)

    override fun getDisciplineScheduledLessonsFlow(
        discipline: Discipline
    ): Flow<List<Lesson>> = lessonLocalDataSource
        .getTeacherDisciplineScheduledLessonsFlow(disciplineID = discipline.id)
        .map { lessonModelList -> lessonModelList.map(LessonModel::toLesson) }
        .flowOn(context = coroutineDispatcher)

    override fun getDisciplineFinishedLessonsFlow(
        discipline: Discipline
    ): Flow<List<Lesson>> = lessonLocalDataSource
        .getTeacherDisciplineFinishedLessonsFlow(disciplineID = discipline.id)
        .map { lessonModelList -> lessonModelList.map(LessonModel::toLesson) }
        .flowOn(context = coroutineDispatcher)

    override suspend fun loadFinishedLessons(): LoadResult<List<Lesson>> =
        safeExecute(context = coroutineDispatcher) {
            val response = lessonRemoteDataSource.getFinishedLessons()

            if (response is Response.Success) {
                val finishedLessonDTOList = response.value
                val finishedLessonModelArray = finishedLessonDTOList
                    .map(LessonDTO::toLessonModel)
                    .toTypedArray()

                lessonLocalDataSource
                    .saveLessonModels(*finishedLessonModelArray)
                    .getOrThrow()
            }

            response.toLoadResult { lessonDTOList ->
                lessonDTOList.map(LessonDTO::toLesson)
            }
        }.unwrap()

    override suspend fun loadDisciplineFinishedLessons(
        discipline: Discipline
    ): LoadResult<List<Lesson>> = safeExecute(context = coroutineDispatcher) {
        val response = lessonRemoteDataSource
            .getDisciplineFinishedLessons(disciplineID = discipline.id)

        if (response is Response.Success) {
            val finishedLessonDTOList = response.value
            val finishedLessonModelArray = finishedLessonDTOList
                .map(LessonDTO::toLessonModel)
                .toTypedArray()

            lessonLocalDataSource
                .saveLessonModels(*finishedLessonModelArray)
                .getOrThrow()
        }

        response.toLoadResult { lessonDTOList ->
            lessonDTOList.map(LessonDTO::toLesson)
        }
    }.unwrap()

    override suspend fun addLesson(lesson: Lesson): Result<Unit> =
        safeExecute(context = coroutineDispatcher) {
            val lessonID = lesson.id
            val groupIDList = lesson.associatedGroups.map { group -> group.id }
            val response = participantRemoteDataSource.getStudentsOfGroups(groupIDList)

            if (response is Response.Success) {
                val studentDTOMap = response.value
                val studentEntityList = studentDTOMap.map { (groupID, studentDTO) ->
                    studentDTO.toStudentEntity(groupID)
                }

                lessonLocalDataSource
                    .addLesson(lessonModel = lesson.toLessonModel())
                    .getOrThrow()

                participantLocalDataSource
                    .attachStudentsToLesson(lessonID, studentEntityList)
                    .getOrThrow()
            }
        }.onFailure {
            removeLesson(lesson)
        }

    override suspend fun updateLesson(lesson: Lesson): Result<Unit> =
        safeExecute(context = coroutineDispatcher) {
            val lessonID = lesson.id
            val groupIDList = lesson.associatedGroups.map { group -> group.id }
            val response = participantRemoteDataSource.getStudentsOfGroups(groupIDList)

            if (response is Response.Success) {
                val studentDTOMap = response.value
                val studentEntityList = studentDTOMap.map { (groupID, studentDTO) ->
                    studentDTO.toStudentEntity(groupID)
                }

                lessonLocalDataSource
                    .updateLesson(lessonModel = lesson.toLessonModel())
                    .getOrThrow()

                participantLocalDataSource
                    .attachStudentsToLesson(lessonID, studentEntityList)
                    .getOrThrow()
            }
        }

    override suspend fun removeLesson(lesson: Lesson): Result<Unit> =
        safeExecute(context = coroutineDispatcher) {
            lessonLocalDataSource.removeLesson(lessonEntity = lesson.toLessonEntity())
        }

    override suspend fun startLesson(lesson: Lesson): Result<Unit> =
        safeExecute(context = coroutineDispatcher) {
            lessonLocalDataSource.startLesson(lessonID = lesson.id)
        }

    override suspend fun finishLesson(lesson: Lesson): Result<Unit> =
        safeExecute(context = coroutineDispatcher) {
            lessonLocalDataSource.finishLesson(lessonID = lesson.id)
        }
}
