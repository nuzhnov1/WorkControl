package com.nuzhnov.workcontrol.core.lesson.data.repository

import com.nuzhnov.workcontrol.core.lesson.data.datasource.ParticipantRemoteDataSource
import com.nuzhnov.workcontrol.core.lesson.data.datasource.ParticipantLocalDataSource
import com.nuzhnov.workcontrol.core.lesson.data.datasource.LessonLocalDataSource
import com.nuzhnov.workcontrol.core.lesson.data.datasource.StudentLocalDataSource
import com.nuzhnov.workcontrol.core.lesson.domen.repository.ParticipantRepository
import com.nuzhnov.workcontrol.core.lesson.domen.exception.LessonException
import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.data.mapper.*
import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.data.preferences.model.Session
import com.nuzhnov.workcontrol.core.models.Participant
import com.nuzhnov.workcontrol.core.models.Student
import com.nuzhnov.workcontrol.core.models.Lesson
import com.nuzhnov.workcontrol.core.models.Role
import com.nuzhnov.workcontrol.core.models.util.LoadResult
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import com.nuzhnov.workcontrol.core.util.roles.requireRole
import com.nuzhnov.workcontrol.core.util.roles.requireTeacherRole
import com.nuzhnov.workcontrol.core.util.roles.requireStudentRole
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class ParticipantRepositoryImpl @Inject constructor(
    private val participantRemoteDataSource: ParticipantRemoteDataSource,
    private val participantLocalDataSource: ParticipantLocalDataSource,
    private val lessonLocalDataSource: LessonLocalDataSource,
    private val studentLocalDataSource: StudentLocalDataSource,
    private val appPreferences: AppPreferences,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ParticipantRepository {

    override fun getParticipantsOfLessonFlow(lesson: Lesson) = flow {
        val flow = appPreferences.requireTeacherRole { session ->
            participantLocalDataSource.getParticipantsOfTeacherLessonFlow(
                teacherID = session.id,
                lessonID = lesson.id
            )
        }.map { modelList -> modelList.map { model -> model.toParticipant(lesson) } }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override fun getStudentParticipationOfTeacherLessonsFlow(student: Student) = flow {
        val flow = appPreferences.requireTeacherRole { session ->
            participantLocalDataSource.getStudentParticipationOfTeacherLessonsFlow(
                teacherID = session.id,
                studentID = student.id
            )
        }.map { modelList -> modelList.map { model -> model.toParticipant(student) } }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override fun getStudentParticipationOfLessonsFlow() = flow {
        val flow = appPreferences.requireStudentRole { session ->
            val student = studentLocalDataSource
                .getCurrentStudentModel(studentID = session.id)
                ?.toStudent()
                ?: throw LessonException("student not found")

            participantLocalDataSource
                .getStudentParticipationOfLessonsFlow(studentID = session.id)
                .map { modelList -> modelList.map { model -> model.toParticipant(student) } }
        }

        emitAll(flow)
    }.flowOn(context = coroutineDispatcher)

    override suspend fun refreshParticipantsOfFinishedLesson(lesson: Lesson) =
        refresh(role = Role.TEACHER) {
            val lessonID = lesson.id
            val response = participantRemoteDataSource.getParticipantsOfFinishedTeacherLesson(lessonID)

            if (response is Response.Success) {
                val modelDTOList = response.value

                val participantEntities = modelDTOList
                    .map { modelDTO -> modelDTO.toParticipantEntity(lessonID) }
                    .toTypedArray()

                val studentEntities = modelDTOList
                    .map { modelDTO -> modelDTO.studentModelDTO.toStudentEntity() }
                    .toTypedArray()

                studentLocalDataSource.saveStudentEntities(*studentEntities)
                participantLocalDataSource.saveParticipantEntities(*participantEntities)
            }

            response.toLoadResult()
        }

    override suspend fun refreshStudentParticipationOfTeacherLessons(student: Student) =
        refresh(role = Role.TEACHER) {
            val studentID = student.id
            val response = participantRemoteDataSource.getStudentParticipationOfFinishedTeacherLessons(studentID)

            if (response is Response.Success) {
                val modelDTOList = response.value

                val participantEntities = modelDTOList
                    .map { modelDTO -> modelDTO.toParticipantEntity(studentID) }
                    .toTypedArray()

                val lessonModels = modelDTOList
                    .map { modelDTO -> modelDTO.lessonDTO.toLessonModel() }
                    .toTypedArray()

                lessonLocalDataSource.saveLessonModels(*lessonModels)
                participantLocalDataSource.saveParticipantEntities(*participantEntities)
            }

            response.toLoadResult()
        }

    override suspend fun refreshStudentParticipationOfLessons() =
        refresh(role = Role.STUDENT) { session ->
            val student = studentLocalDataSource
                .getCurrentStudentModel(studentID = session.id)
                ?.toStudent()
                ?: throw LessonException("student not found")

            val response = participantRemoteDataSource.getStudentParticipationOfFinishedLessons()

            if (response is Response.Success) {
                val modelDTOList = response.value

                val participantEntities = modelDTOList
                    .map { modelDTO -> modelDTO.toParticipantEntity(student.id) }
                    .toTypedArray()

                val lessonModels = modelDTOList
                    .map { modelDTO -> modelDTO.lessonDTO.toLessonModel() }
                    .toTypedArray()

                lessonLocalDataSource.saveLessonModels(*lessonModels)
                participantLocalDataSource.saveParticipantEntities(*participantEntities)
            }

            response.toLoadResult()
        }

    override suspend fun updateParticipant(participant: Participant) =
        executeOperation(role = Role.TEACHER) {
            participantLocalDataSource.updateParticipant(
                participantUpdatableModel = participant.toParticipantUpdatableModel()
            )
        }

    private suspend fun refresh(role: Role, block: suspend (Session) -> LoadResult) =
        withContext(context = coroutineDispatcher) {
            val session = appPreferences.requireRole(role)

            safeExecute { block(session) }
                .onFailure { cause ->
                    if (cause is LessonException) {
                        throw cause
                    }
                }.unwrap()
        }

    private suspend fun executeOperation(role: Role, block: suspend (Session) -> Unit) =
        withContext(context = coroutineDispatcher) {
            val session = appPreferences.requireRole(role)

            safeExecute { block(session) }
                .onFailure { cause ->
                    if (cause is LessonException) {
                        throw cause
                    }
                }.toOperationResult()
        }
}
