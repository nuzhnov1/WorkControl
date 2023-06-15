package com.nuzhnov.workcontrol.core.lesson.domen.usecase.participant

import com.nuzhnov.workcontrol.core.lesson.domen.repository.ParticipantRepository
import javax.inject.Inject

class GetStudentParticipationOfLessonsFlowUseCase @Inject internal constructor(
    private val participantRepository: ParticipantRepository
) {

    operator fun invoke() = participantRepository.getStudentParticipationOfLessonsFlow()
}
