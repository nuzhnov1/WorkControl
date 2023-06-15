package com.nuzhnov.workcontrol.core.lesson.domen.repository

import com.nuzhnov.workcontrol.core.models.Participant
import com.nuzhnov.workcontrol.core.models.Lesson
import com.nuzhnov.workcontrol.core.models.Student
import com.nuzhnov.workcontrol.core.models.util.LoadResult
import com.nuzhnov.workcontrol.core.models.util.OperationResult
import kotlinx.coroutines.flow.Flow

internal interface ParticipantRepository {
    fun getParticipantsOfLessonFlow(lesson: Lesson): Flow<List<Participant>>

    fun getStudentParticipationOfTeacherLessonsFlow(student: Student): Flow<List<Participant>>

    fun getStudentParticipationOfLessonsFlow(): Flow<List<Participant>>

    suspend fun refreshParticipantsOfFinishedLesson(lesson: Lesson): LoadResult

    suspend fun refreshStudentParticipationOfTeacherLessons(student: Student): LoadResult

    suspend fun refreshStudentParticipationOfLessons(): LoadResult

    suspend fun updateParticipant(participant: Participant): OperationResult
}
