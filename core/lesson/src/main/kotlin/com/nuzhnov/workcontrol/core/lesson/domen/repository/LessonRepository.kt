package com.nuzhnov.workcontrol.core.lesson.domen.repository

import com.nuzhnov.workcontrol.core.model.Discipline
import com.nuzhnov.workcontrol.core.model.Lesson
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import kotlinx.coroutines.flow.Flow

internal interface LessonRepository {
    fun getCreatedLessonsFlow(): Flow<List<Lesson>>

    fun getActiveLessonFlow(): Flow<Lesson?>

    fun getFinishedLessonsFlow(): Flow<List<Lesson>>

    fun getDisciplineCreatedLessonsFlow(discipline: Discipline): Flow<List<Lesson>>

    fun getDisciplineFinishedLessonsFlow(discipline: Discipline): Flow<List<Lesson>>

    suspend fun loadFinishedLessons(): LoadResult<List<Lesson>>

    suspend fun loadDisciplineFinishedLessons(discipline: Discipline): LoadResult<List<Lesson>>

    suspend fun addLesson(lesson: Lesson): Result<Unit>

    suspend fun updateLesson(lesson: Lesson): Result<Unit>

    suspend fun removeLesson(lesson: Lesson): Result<Unit>

    suspend fun startLesson(lesson: Lesson): Result<Unit>

    suspend fun finishLesson(lesson: Lesson): Result<Unit>
}
