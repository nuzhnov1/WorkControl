package com.nuzhnov.workcontrol.core.lesson.domen.usecase.lesson

import com.nuzhnov.workcontrol.core.lesson.domen.repository.LessonRepository
import com.nuzhnov.workcontrol.core.models.util.LoadResult
import javax.inject.Inject

class RefreshFinishedLessonsUseCase @Inject internal constructor(
    private val repository: LessonRepository
) {

    suspend operator fun invoke(): LoadResult = repository.refreshFinishedLessons()
}
