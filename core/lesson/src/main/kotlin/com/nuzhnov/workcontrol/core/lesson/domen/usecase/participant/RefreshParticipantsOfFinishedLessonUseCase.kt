package com.nuzhnov.workcontrol.core.lesson.domen.usecase.participant

import com.nuzhnov.workcontrol.core.lesson.domen.repository.ParticipantRepository
import com.nuzhnov.workcontrol.core.models.Lesson
import javax.inject.Inject

class RefreshParticipantsOfFinishedLessonUseCase @Inject internal constructor(
    private val repository: ParticipantRepository
) {

    suspend operator fun invoke(lesson: Lesson) = repository.refreshParticipantsOfFinishedLesson(lesson)
}
