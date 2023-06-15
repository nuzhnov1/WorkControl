package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.ParticipantEntityModel
import com.nuzhnov.workcontrol.core.data.database.entity.model.ParticipantWithLessonEntityModel
import com.nuzhnov.workcontrol.core.data.database.entity.partial.ParticipantPartialEntity
import com.nuzhnov.workcontrol.core.data.database.entity.partial.ParticipantActivityPartialEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.*

@Dao
abstract class ParticipantDAO : EntityDAO<ParticipantEntity>(entityName = "participant") {
    @Transaction
    @Query("""
        SELECT participant.* FROM participant
            INNER JOIN lesson ON participant.lesson_id = lesson.id
        WHERE participant.lesson_id = :lessonID AND lesson.teacher_id = :teacherID
    """)
    abstract fun getParticipantsOfTeacherLessonFlow(
        teacherID: Long,
        lessonID: Long
    ): Flow<List<ParticipantEntityModel>>

    @Transaction
    @Query("""
        SELECT participant.* FROM participant
            INNER JOIN lesson ON participant.lesson_id = lesson.id
        WHERE participant.student_id = :studentID AND lesson.teacher_id = :teacherID
    """)
    abstract fun getStudentParticipationOfTeacherLessonsFlow(
        teacherID: Long,
        studentID: Long
    ): Flow<List<ParticipantWithLessonEntityModel>>

    @Transaction
    @Query("SELECT * FROM participant WHERE student_id = :studentID")
    abstract fun getStudentParticipationOfLessonsFlow(
        studentID: Long
    ): Flow<List<ParticipantWithLessonEntityModel>>

    @Query("SELECT * FROM participant WHERE lesson_id = :lessonID")
    abstract suspend fun getEntitiesByLessonID(lessonID: Long): List<ParticipantEntity>

    @Query("SELECT * FROM participant WHERE lesson_id IN (:lessonIDList)")
    abstract suspend fun getEntitiesByLessonIDList(lessonIDList: List<Long>): List<ParticipantEntity>

    @Update(entity = ParticipantEntity::class)
    abstract suspend fun updateData(
        vararg participantPartialEntity: ParticipantPartialEntity
    )

    @Update(entity = ParticipantEntity::class)
    abstract suspend fun updateActivity(
        vararg participantActivityPartialEntity: ParticipantActivityPartialEntity
    )
}
