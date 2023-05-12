package com.nuzhnov.workcontrol.core.lesson.domen.usecase.lesson

import com.nuzhnov.workcontrol.core.lesson.domen.repository.LessonRepository
import com.nuzhnov.workcontrol.core.model.Lesson
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFinishedLessonsFlowUseCase @Inject internal constructor(
    private val repository: LessonRepository
) {

    operator fun invoke(): Flow<List<Lesson>> = repository.getFinishedLessonsFlow()
}
