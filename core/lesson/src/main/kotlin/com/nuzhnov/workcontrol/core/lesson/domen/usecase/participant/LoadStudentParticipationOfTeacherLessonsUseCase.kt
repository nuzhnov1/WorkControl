package com.nuzhnov.workcontrol.core.lesson.domen.usecase.participant

import com.nuzhnov.workcontrol.core.lesson.domen.repository.ParticipantRepository
import com.nuzhnov.workcontrol.core.model.Participant
import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import javax.inject.Inject

class LoadStudentParticipationOfTeacherLessonsUseCase @Inject internal constructor(
    private val repository: ParticipantRepository
) {

    suspend operator fun invoke(
        student: Student
    ): LoadResult<List<Participant>> = repository.loadStudentParticipationOfTeacherLessons(student)
}
