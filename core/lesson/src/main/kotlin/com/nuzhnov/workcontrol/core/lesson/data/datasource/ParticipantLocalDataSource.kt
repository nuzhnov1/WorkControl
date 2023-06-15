package com.nuzhnov.workcontrol.core.lesson.data.datasource

import com.nuzhnov.workcontrol.core.data.database.dao.ParticipantDAO
import com.nuzhnov.workcontrol.core.data.database.dao.StudentDAO
import com.nuzhnov.workcontrol.core.data.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.update.ParticipantUpdatableModel
import com.nuzhnov.workcontrol.core.data.database.entity.StudentEntity
import javax.inject.Inject

internal class ParticipantLocalDataSource @Inject constructor(
    private val studentDAO: StudentDAO,
    private val participantDAO: ParticipantDAO
) {

    fun getParticipantsOfTeacherLessonFlow(teacherID: Long, lessonID: Long) =
        participantDAO.getParticipantsOfTeacherLessonFlow(teacherID, lessonID)

    fun getStudentParticipationOfTeacherLessonsFlow(teacherID: Long, studentID: Long) =
        participantDAO.getStudentParticipationOfTeacherLessonsFlow(teacherID, studentID)

    fun getStudentParticipationOfLessonsFlow(studentID: Long) =
        participantDAO.getStudentParticipationOfLessonsFlow(studentID)

    suspend fun saveParticipantEntities(vararg participantEntity: ParticipantEntity) =
        participantDAO.insertOrUpdate(*participantEntity)

    suspend fun attachStudentsToLesson(lessonID: Long, studentEntityList: List<StudentEntity>) {
        studentDAO.insertOrUpdate(*studentEntityList.toTypedArray())

        val attachedParticipantArray = studentEntityList.map { entity ->
            ParticipantEntity(
                studentID = entity.id,
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
        }.toTypedArray()

        participantDAO.insertOrUpdate(*attachedParticipantArray)
    }

    suspend fun updateParticipant(participantUpdatableModel: ParticipantUpdatableModel) =
        participantDAO.updateData(participantUpdatableModel)
}
