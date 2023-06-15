package com.nuzhnov.workcontrol.core.lesson.domen.usecase.participant

import com.nuzhnov.workcontrol.core.lesson.domen.repository.ParticipantRepository
import com.nuzhnov.workcontrol.core.models.Lesson
import javax.inject.Inject

class GetParticipantsOfLessonFlowUseCase @Inject internal constructor(
    private val participantRepository: ParticipantRepository
) {

    operator fun invoke(lesson: Lesson) = participantRepository.getParticipantsOfLessonFlow(lesson)
}
