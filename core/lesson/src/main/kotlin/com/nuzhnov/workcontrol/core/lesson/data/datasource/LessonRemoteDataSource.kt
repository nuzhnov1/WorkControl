package com.nuzhnov.workcontrol.core.lesson.data.datasource

import com.nuzhnov.workcontrol.core.data.api.service.LessonService
import com.nuzhnov.workcontrol.core.data.api.dto.lesson.LessonDTO
import com.nuzhnov.workcontrol.core.data.api.util.Response
import com.nuzhnov.workcontrol.core.data.api.util.safeApiCall
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class LessonRemoteDataSource @Inject constructor(
    private val lessonService: LessonService,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun getFinishedLessons(): Response<List<LessonDTO>> =
        safeApiCall(context = coroutineDispatcher) {
            lessonService.getFinishedLessons()
        }

    suspend fun getDisciplineFinishedLessons(disciplineID: Long): Response<List<LessonDTO>> =
        safeApiCall(context = coroutineDispatcher) {
            lessonService.getDisciplineFinishedLessons(disciplineID)
        }
}
