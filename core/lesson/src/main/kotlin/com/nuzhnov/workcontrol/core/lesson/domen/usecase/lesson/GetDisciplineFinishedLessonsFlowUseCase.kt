package com.nuzhnov.workcontrol.core.lesson.domen.usecase.lesson

import com.nuzhnov.workcontrol.core.lesson.domen.repository.LessonRepository
import com.nuzhnov.workcontrol.core.model.Lesson
import com.nuzhnov.workcontrol.core.model.Discipline
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDisciplineFinishedLessonsFlowUseCase @Inject internal constructor(
    private val repository: LessonRepository
) {

    operator fun invoke(
        discipline: Discipline
    ): Flow<List<Lesson>> = repository.getDisciplineFinishedLessonsFlow(discipline)
}
