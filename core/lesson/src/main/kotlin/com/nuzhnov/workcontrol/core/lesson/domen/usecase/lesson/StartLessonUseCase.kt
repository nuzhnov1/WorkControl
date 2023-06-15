package com.nuzhnov.workcontrol.core.lesson.domen.usecase.lesson

import com.nuzhnov.workcontrol.core.lesson.domen.repository.LessonRepository
import com.nuzhnov.workcontrol.core.lesson.domen.exception.LessonException
import com.nuzhnov.workcontrol.core.models.Lesson
import javax.inject.Inject

class StartLessonUseCase @Inject internal constructor(
    private val repository: LessonRepository
) {

    suspend operator fun invoke(lesson: Lesson) = when (lesson.state) {
        Lesson.State.SCHEDULED -> repository.startLesson(lesson)
        Lesson.State.ACTIVE -> throw LessonException("the lesson is already active")
        Lesson.State.FINISHED -> throw LessonException("the lesson is finished")
    }
}
