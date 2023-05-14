package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.ParticipantModel
import com.nuzhnov.workcontrol.core.data.database.entity.model.ParticipantLessonModel
import com.nuzhnov.workcontrol.core.data.database.entity.model.update.ParticipantUpdatableModel
import com.nuzhnov.workcontrol.core.data.database.entity.model.update.ParticipantActivityModel
import kotlinx.coroutines.flow.Flow
import androidx.room.*

@Dao
interface ParticipantDAO : BaseDAO<ParticipantEntity> {
    @[Transaction Query(FETCH_BY_TEACHER_ID_AND_LESSON_ID_QUERY)]
    fun getParticipantsOfTeacherLessonFlow(
        teacherID: Long,
        lessonID: Long
    ): Flow<List<ParticipantModel>>

    @[Transaction Query(FETCH_BY_TEACHER_ID_AND_STUDENT_ID_QUERY)]
    fun getStudentParticipationOfTeacherLessonsFlow(
        teacherID: Long,
        studentID: Long
    ): Flow<List<ParticipantLessonModel>>

    @[Transaction Query(FETCH_BY_STUDENT_ID_QUERY)]
    fun getStudentParticipationOfLessonsFlow(
        studentID: Long
    ): Flow<List<ParticipantLessonModel>>

    @Query(FETCH_BY_LESSON_ID_QUERY)
    suspend fun getEntities(lessonID: Long): List<ParticipantEntity>

    @Query(FETCH_BY_LESSON_ID_LIST_QUERY)
    suspend fun getEntities(lessonIDList: List<Long>): List<ParticipantEntity>

    @Update(entity = ParticipantEntity::class)
    suspend fun updateData(vararg participantUpdatableModel: ParticipantUpdatableModel)

    @Update(entity = ParticipantEntity::class)
    suspend fun updateActivity(vararg participantActivityModel: ParticipantActivityModel)


    private companion object {
        const val FETCH_BY_TEACHER_ID_AND_LESSON_ID_QUERY = """
            SELECT participant.* FROM participant
                INNER JOIN lesson ON participant.lesson_id = lesson.id
            WHERE participant.lesson_id = :lessonID AND lesson.teacher_id = :teacherID
        """

        const val FETCH_BY_TEACHER_ID_AND_STUDENT_ID_QUERY = """
            SELECT participant.* FROM participant
                INNER JOIN lesson ON participant.lesson_id = lesson.id
            WHERE participant.student_id = :studentID AND lesson.teacher_id = :teacherID
        """

        const val FETCH_BY_STUDENT_ID_QUERY = """
            SELECT * FROM participant WHERE student_id = :studentID
        """

        const val FETCH_BY_LESSON_ID_QUERY = """
            SELECT * FROM participant WHERE lesson_id = :lessonID
        """

        const val FETCH_BY_LESSON_ID_LIST_QUERY = """
            SELECT * FROM participant WHERE lesson_id IN (:lessonIDList)
        """
    }
}
