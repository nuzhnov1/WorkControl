package com.nuzhnov.workcontrol.core.lesson.domen.usecase.participant

import com.nuzhnov.workcontrol.core.lesson.domen.repository.ParticipantRepository
import com.nuzhnov.workcontrol.core.models.Student
import javax.inject.Inject

class RefreshStudentParticipationOfTeacherLessonsUseCase @Inject internal constructor(
    private val repository: ParticipantRepository
) {

    suspend operator fun invoke(student: Student) = repository.refreshStudentParticipationOfTeacherLessons(student)
}
