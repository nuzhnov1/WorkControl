package com.nuzhnov.workcontrol.core.lesson.domen.usecase.participant

import com.nuzhnov.workcontrol.core.lesson.domen.repository.ParticipantRepository
import com.nuzhnov.workcontrol.core.models.Participant
import javax.inject.Inject

class UpdateParticipantUseCase @Inject internal constructor(
    private val repository: ParticipantRepository
) {

    suspend operator fun invoke(participant: Participant) = repository.updateParticipant(participant)
}
