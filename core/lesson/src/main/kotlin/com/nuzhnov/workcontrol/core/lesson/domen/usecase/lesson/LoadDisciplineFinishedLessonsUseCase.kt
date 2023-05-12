package com.nuzhnov.workcontrol.core.lesson.domen.usecase.lesson

import com.nuzhnov.workcontrol.core.lesson.domen.repository.LessonRepository
import com.nuzhnov.workcontrol.core.model.Lesson
import com.nuzhnov.workcontrol.core.model.Discipline
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import javax.inject.Inject

class LoadDisciplineFinishedLessonsUseCase @Inject internal constructor(
    private val repository: LessonRepository
) {

    suspend operator fun invoke(
        discipline: Discipline
    ): LoadResult<List<Lesson>> = repository.loadDisciplineFinishedLessons(discipline)
}
