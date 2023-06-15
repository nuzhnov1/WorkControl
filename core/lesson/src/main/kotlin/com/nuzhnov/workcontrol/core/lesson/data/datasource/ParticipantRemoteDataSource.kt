package com.nuzhnov.workcontrol.core.lesson.data.datasource

import com.nuzhnov.workcontrol.core.data.api.service.LessonService
import com.nuzhnov.workcontrol.core.data.api.service.UniversityService
import com.nuzhnov.workcontrol.core.data.api.util.safeApiCall
import javax.inject.Inject

internal class ParticipantRemoteDataSource @Inject constructor(
    private val lessonService: LessonService,
    private val universityService: UniversityService
) {

    suspend fun getParticipantsOfFinishedTeacherLesson(lessonID: Long) =
        safeApiCall { lessonService.getParticipantsOfFinishedTeacherLesson(lessonID) }

    suspend fun getStudentParticipationOfFinishedTeacherLessons(studentID: Long) =
        safeApiCall { lessonService.getStudentParticipationOfFinishedTeacherLessons(studentID) }

    suspend fun getStudentParticipationOfFinishedLessons() =
        safeApiCall { lessonService.getStudentParticipationOfFinishedLessons() }

    suspend fun getStudentsOfGroups(groupIDList: List<Long>) =
        safeApiCall { universityService.getStudentsOfGroups(groupIDList) }
}
