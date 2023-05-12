package com.nuzhnov.workcontrol.core.lesson.domen.usecase.participant

import com.nuzhnov.workcontrol.core.lesson.domen.repository.ParticipantRepository
import com.nuzhnov.workcontrol.core.model.Participant
import com.nuzhnov.workcontrol.core.model.Lesson
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetParticipantsOfLessonFlowUseCase @Inject internal constructor(
    private val participantRepository: ParticipantRepository
) {

    operator fun invoke(lesson: Lesson): Flow<LoadResult<List<Participant>>> =
        participantRepository.getParticipantsOfLessonFlow(lesson)
}
