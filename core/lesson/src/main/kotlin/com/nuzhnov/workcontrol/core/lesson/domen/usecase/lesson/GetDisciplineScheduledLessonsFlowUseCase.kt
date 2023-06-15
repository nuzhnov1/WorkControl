package com.nuzhnov.workcontrol.core.lesson.domen.usecase.lesson

import com.nuzhnov.workcontrol.core.lesson.domen.repository.LessonRepository
import com.nuzhnov.workcontrol.core.models.Discipline
import javax.inject.Inject

class GetDisciplineScheduledLessonsFlowUseCase @Inject internal constructor(
    private val repository: LessonRepository
) {

    operator fun invoke(discipline: Discipline) = repository.getDisciplineScheduledLessonsFlow(discipline)
}
