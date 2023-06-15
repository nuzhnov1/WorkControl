package com.nuzhnov.workcontrol.core.lesson.domen.repository

import com.nuzhnov.workcontrol.core.models.Lesson
import com.nuzhnov.workcontrol.core.models.Discipline
import com.nuzhnov.workcontrol.core.models.util.LoadResult
import com.nuzhnov.workcontrol.core.models.util.OperationResult
import kotlinx.coroutines.flow.Flow

internal interface LessonRepository {
    fun getScheduledLessonsFlow(): Flow<List<Lesson>>

    fun getActiveLessonFlow(): Flow<Lesson?>

    fun getFinishedLessonsFlow(): Flow<List<Lesson>>

    fun getDisciplineScheduledLessonsFlow(discipline: Discipline): Flow<List<Lesson>>

    fun getDisciplineFinishedLessonsFlow(discipline: Discipline): Flow<List<Lesson>>

    suspend fun refreshFinishedLessons(): LoadResult

    suspend fun refreshDisciplineFinishedLessons(discipline: Discipline): LoadResult

    suspend fun addLesson(lesson: Lesson): OperationResult

    suspend fun updateLesson(lesson: Lesson): OperationResult

    suspend fun removeLesson(lesson: Lesson): OperationResult

    suspend fun startLesson(lesson: Lesson): OperationResult

    suspend fun finishLesson(lesson: Lesson): OperationResult
}
