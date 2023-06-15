package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.LessonEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.LessonEntityModel
import com.nuzhnov.workcontrol.core.data.database.entity.model.LessonWithParticipantsEntityModel
import kotlinx.coroutines.flow.Flow
import androidx.room.*

@Dao
abstract class LessonDAO : EntityDAO<LessonEntity>(entityName = "lesson") {
    @Query("SELECT * FROM lesson WHERE id = :lessonID")
    abstract suspend fun getEntity(lessonID: Long): LessonEntity?

    @Transaction
    @Query("SELECT * FROM lesson WHERE state = 'SCHEDULED' AND teacher_id = :teacherID")
    abstract fun getTeacherScheduledLessonsFlow(teacherID: Long): Flow<List<LessonEntityModel>>

    @Transaction
    @Query("SELECT * FROM lesson WHERE state = 'ACTIVE' AND teacher_id = :teacherID")
    abstract suspend fun getTeacherActiveLesson(teacherID: Long): LessonEntityModel?

    @Transaction
    @Query("SELECT * FROM lesson WHERE state = 'ACTIVE' AND teacher_id = :teacherID")
    abstract fun getTeacherActiveLessonFlow(teacherID: Long): Flow<LessonEntityModel?>

    @Transaction
    @Query("SELECT * FROM lesson WHERE state = 'FINISHED' AND teacher_id = :teacherID")
    abstract fun getTeacherFinishedLessonFlow(teacherID: Long): Flow<List<LessonEntityModel>>

    @Transaction
    @Query("""
        SELECT * FROM lesson
        WHERE state = 'SCHEDULED' AND teacher_id = :teacherID AND discipline_id = :disciplineID
    """)
    abstract fun getTeacherDisciplineScheduledLessonsFlow(
        teacherID: Long,
        disciplineID: Long
    ): Flow<List<LessonEntityModel>>

    @Transaction
    @Query("""
        SELECT * FROM lesson
        WHERE state = 'FINISHED' AND teacher_id = :teacherID AND discipline_id = :disciplineID
    """)
    abstract fun getTeacherDisciplineFinishedLessonsFlow(
        teacherID: Long,
        disciplineID: Long
    ): Flow<List<LessonEntityModel>>

    @Query("SELECT * FROM lesson WHERE state = 'FINISHED'")
    abstract suspend fun getFinishedLessonEntities(): List<LessonEntity>

    @Transaction
    @Query("SELECT * FROM lesson WHERE state = 'FINISHED'")
    abstract suspend fun getFinishedEntitiesWithParticipants(): List<LessonWithParticipantsEntityModel>
}
