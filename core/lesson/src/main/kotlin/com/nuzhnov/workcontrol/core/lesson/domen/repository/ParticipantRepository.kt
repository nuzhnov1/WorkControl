package com.nuzhnov.workcontrol.core.lesson.domen.repository

import com.nuzhnov.workcontrol.core.model.Participant
import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.model.Lesson
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import kotlinx.coroutines.flow.Flow

internal interface ParticipantRepository {
    fun getParticipantsOfLessonFlow(lesson: Lesson): Flow<LoadResult<List<Participant>>>

    fun getStudentParticipationOfTeacherLessonsFlow(student: Student): Flow<LoadResult<List<Participant>>>

    fun getStudentParticipationOfLessonsFlow(): Flow<LoadResult<List<Participant>>>

    suspend fun loadParticipantsOfFinishedLesson(lesson: Lesson): LoadResult<List<Participant>>

    suspend fun loadStudentParticipationOfTeacherLessons(student: Student): LoadResult<List<Participant>>

    suspend fun loadStudentParticipationOfLessons(): LoadResult<List<Participant>>

    suspend fun updateParticipant(participant: Participant): Result<Unit>
}
