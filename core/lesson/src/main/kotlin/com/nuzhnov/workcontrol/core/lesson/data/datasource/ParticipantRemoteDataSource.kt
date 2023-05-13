package com.nuzhnov.workcontrol.core.lesson.data.datasource

import com.nuzhnov.workcontrol.core.api.service.LessonService
import com.nuzhnov.workcontrol.core.api.service.UniversityService
import com.nuzhnov.workcontrol.core.api.dto.lesson.ParticipantModelDTO
import com.nuzhnov.workcontrol.core.api.dto.lesson.ParticipantLessonModelDTO
import com.nuzhnov.workcontrol.core.api.dto.university.StudentDTO
import com.nuzhnov.workcontrol.core.api.util.Response
import com.nuzhnov.workcontrol.core.api.util.safeApiCall
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class ParticipantRemoteDataSource @Inject constructor(
    private val lessonService: LessonService,
    private val universityService: UniversityService,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun getParticipantsOfFinishedTeacherLesson(
        lessonID: Long
    ): Response<List<ParticipantModelDTO>> = safeApiCall(context = coroutineDispatcher) {
        lessonService.getParticipantsOfFinishedTeacherLesson(lessonID)
    }

    suspend fun getStudentParticipationOfFinishedTeacherLessons(
        studentID: Long
    ): Response<List<ParticipantLessonModelDTO>> = safeApiCall(context = coroutineDispatcher) {
        lessonService.getStudentParticipationOfFinishedTeacherLessons(studentID)
    }

    suspend fun getStudentParticipationOfFinishedLessons(): Response<List<ParticipantLessonModelDTO>> =
        safeApiCall(context = coroutineDispatcher) {
            lessonService.getStudentParticipationOfFinishedLessons()
        }

    suspend fun getStudentsOfGroups(
        groupIDList: List<Long>
    ): Response<Map<Long, StudentDTO>> = safeApiCall(context = coroutineDispatcher) {
        universityService.getStudentsOfGroups(groupIDList)
    }
}
