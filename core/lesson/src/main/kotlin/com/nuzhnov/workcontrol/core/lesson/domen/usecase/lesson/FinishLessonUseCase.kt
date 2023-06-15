package com.nuzhnov.workcontrol.core.lesson.domen.usecase.lesson

import com.nuzhnov.workcontrol.core.lesson.domen.repository.LessonRepository
import com.nuzhnov.workcontrol.core.lesson.domen.exception.LessonException
import com.nuzhnov.workcontrol.core.models.Lesson
import javax.inject.Inject

class FinishLessonUseCase @Inject internal constructor(
    private val repository: LessonRepository
) {

    suspend operator fun invoke(lesson: Lesson) = when (lesson.state) {
        Lesson.State.FINISHED -> throw LessonException("the lesson is already finished")
        else -> repository.finishLesson(lesson)
    }
}
