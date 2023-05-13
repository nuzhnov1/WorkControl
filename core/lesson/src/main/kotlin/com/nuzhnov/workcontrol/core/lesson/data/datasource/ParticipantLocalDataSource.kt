package com.nuzhnov.workcontrol.core.lesson.data.datasource

import com.nuzhnov.workcontrol.core.database.dao.ParticipantDAO
import com.nuzhnov.workcontrol.core.database.dao.StudentDAO
import com.nuzhnov.workcontrol.core.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.database.entity.model.ParticipantModel
import com.nuzhnov.workcontrol.core.database.entity.model.ParticipantLessonModel
import com.nuzhnov.workcontrol.core.database.entity.model.update.ParticipantUpdatableModel
import com.nuzhnov.workcontrol.core.database.entity.StudentEntity
import com.nuzhnov.workcontrol.core.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.mapper.toStudent
import com.nuzhnov.workcontrol.core.model.Role
import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

internal class ParticipantLocalDataSource @Inject constructor(
    private val studentDAO: StudentDAO,
    private val participantDAO: ParticipantDAO,
    private val appPreferences: AppPreferences,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
) {

    fun getParticipantsOfTeacherLessonFlow(
        lessonID: Long
    ): Flow<List<ParticipantModel>> = appPreferences
        .getSessionFlow()
        .transform { session ->
            if (session?.role == Role.TEACHER) {
                participantDAO
                    .getParticipantsOfTeacherLessonFlow(teacherID = session.id, lessonID)
                    .flowOn(context = coroutineDispatcher)
                    .collect { participantModelList -> emit(participantModelList) }
            } else {
                throw IllegalStateException("permission denied")
            }
        }
        .flowOn(context = coroutineDispatcher)

    fun getStudentParticipationOfTeacherLessonsFlow(
        studentID: Long
    ): Flow<List<ParticipantLessonModel>> = appPreferences
        .getSessionFlow()
        .transform { session ->
            if (session?.role == Role.TEACHER) {
                participantDAO
                    .getStudentParticipationOfTeacherLessonsFlow(teacherID = session.id, studentID)
                    .flowOn(context = coroutineDispatcher)
                    .collect { participantLessonModel -> emit(participantLessonModel) }
            } else {
                throw IllegalStateException("permission denied")
            }
        }
        .flowOn(context = coroutineDispatcher)

    fun getStudentParticipationOfLessonsFlow(): Flow<Pair<Student, List<ParticipantLessonModel>>> =
        appPreferences
            .getSessionFlow()
            .transform { session ->
                if (session?.role == Role.STUDENT) {
                    val student = studentDAO.getStudent(id = session.id)
                        ?.toStudent()
                        ?: throw IllegalStateException("student not found")

                    participantDAO
                        .getStudentParticipationOfLessonsFlow(studentID = session.id)
                        .flowOn(context = coroutineDispatcher)
                        .collect { participantLessonModelList ->
                            emit(value = student to participantLessonModelList)
                        }
                } else {
                    throw IllegalStateException("permission denied")
                }
            }
            .flowOn(context = coroutineDispatcher)

    suspend fun saveParticipantEntities(
        vararg participantEntity: ParticipantEntity
    ): Result<Unit> = safeExecute(context = coroutineDispatcher) {
        participantDAO.insertOrUpdate(*participantEntity)
    }

    suspend fun attachStudentsToLesson(
        lessonID: Long,
        studentEntityList: List<StudentEntity>
    ): Result<Unit> = safeExecute(context = coroutineDispatcher) {
        studentDAO.insertOrUpdate(*studentEntityList.toTypedArray())

        val attachedParticipantArray = studentEntityList
            .map { studentEntity -> studentEntity.id }
            .map { studentID ->
                ParticipantEntity(
                    studentID = studentID,
                    lessonID = lessonID,
                    isActive = false,
                    lastVisit = null,
                    totalVisitDuration = 0.0,
                    isMarked = false,
                    theoryAssessment = null,
                    practiceAssessment = null,
                    activityAssessment = null,
                    prudenceAssessment = null,
                    creativityAssessment = null,
                    preparationAssessment = null,
                    note = null,
                    isSynchronized = false
                )
            }
            .toTypedArray()

        participantDAO.insertOrUpdate(*attachedParticipantArray)
    }

    suspend fun updateParticipant(
        participantUpdatableModel: ParticipantUpdatableModel
    ): Result<Unit> = safeExecute(context = coroutineDispatcher) {
        participantDAO.updateData(participantUpdatableModel)
    }
}
