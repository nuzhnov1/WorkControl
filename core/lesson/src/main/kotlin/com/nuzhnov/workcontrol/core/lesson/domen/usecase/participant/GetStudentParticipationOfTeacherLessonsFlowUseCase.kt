package com.nuzhnov.workcontrol.core.lesson.domen.usecase.participant

import com.nuzhnov.workcontrol.core.lesson.domen.repository.ParticipantRepository
import com.nuzhnov.workcontrol.core.models.Student
import javax.inject.Inject

class GetStudentParticipationOfTeacherLessonsFlowUseCase @Inject internal constructor(
    private val participantRepository: ParticipantRepository
) {

    operator fun invoke(student: Student) = participantRepository.getStudentParticipationOfTeacherLessonsFlow(student)
}
