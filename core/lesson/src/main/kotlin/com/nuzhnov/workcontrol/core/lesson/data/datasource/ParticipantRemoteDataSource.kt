package com.nuzhnov.workcontrol.core.lesson.data.datasource

import com.nuzhnov.workcontrol.core.data.api.service.LessonService
import com.nuzhnov.workcontrol.core.data.api.service.UniversityService
import com.nuzhnov.workcontrol.core.data.api.dto.lesson.ParticipantModelDTO
import com.nuzhnov.workcontrol.core.data.api.dto.lesson.ParticipantLessonModelDTO
import com.nuzhnov.workcontrol.core.data.api.dto.university.StudentDTO
import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.data.api.util.safeApiCall
import javax.inject.Inject

internal class ParticipantRemoteDataSource @Inject constructor(
    private val lessonService: LessonService,
    private val universityService: UniversityService
) {

    suspend fun getParticipantsOfFinishedTeacherLesson(
        lessonID: Long
    ): Response<List<ParticipantModelDTO>> = safeApiCall {
        lessonService.getParticipantsOfFinishedTeacherLesson(lessonID)
    }

    suspend fun getStudentParticipationOfFinishedTeacherLessons(
        studentID: Long
    ): Response<List<ParticipantLessonModelDTO>> = safeApiCall {
        lessonService.getStudentParticipationOfFinishedTeacherLessons(studentID)
    }

    suspend fun getStudentParticipationOfFinishedLessons(): Response<List<ParticipantLessonModelDTO>> =
        safeApiCall { lessonService.getStudentParticipationOfFinishedLessons() }

    suspend fun getStudentsOfGroups(
        groupIDList: List<Long>
    ): Response<Map<Long, StudentDTO>> = safeApiCall {
        universityService.getStudentsOfGroups(groupIDList)
    }
}
