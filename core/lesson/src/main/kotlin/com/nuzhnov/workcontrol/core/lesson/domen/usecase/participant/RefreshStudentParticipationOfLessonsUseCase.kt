package com.nuzhnov.workcontrol.core.lesson.domen.usecase.participant

import com.nuzhnov.workcontrol.core.lesson.domen.repository.ParticipantRepository
import javax.inject.Inject

class RefreshStudentParticipationOfLessonsUseCase @Inject internal constructor(
    private val repository: ParticipantRepository
) {

    suspend operator fun invoke() = repository.refreshStudentParticipationOfLessons()
}
