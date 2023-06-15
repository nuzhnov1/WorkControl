package com.nuzhnov.workcontrol.core.lesson.domen.usecase.lesson

import com.nuzhnov.workcontrol.core.lesson.domen.repository.LessonRepository
import javax.inject.Inject

class GetActiveLessonsFlowUseCase @Inject internal constructor(
    private val repository: LessonRepository
) {

    operator fun invoke() = repository.getActiveLessonFlow()
}
