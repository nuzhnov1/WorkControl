package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.database.entity.model.ParticipantModel
import com.nuzhnov.workcontrol.core.database.entity.model.ParticipantLessonModel
import com.nuzhnov.workcontrol.core.database.entity.model.update.ParticipantUpdatableModel
import com.nuzhnov.workcontrol.core.database.entity.model.update.ParticipantActivityModel
import kotlinx.coroutines.flow.Flow
import androidx.room.*

@Dao
interface ParticipantDao : BaseDao<ParticipantEntity> {
    @[Transaction Query(FETCH_BY_LESSON_ID_QUERY)]
    fun getParticipantsFlow(lessonID: Long): Flow<List<ParticipantModel>>

    @[Transaction Query(FETCH_BY_STUDENT_ID_QUERY)]
    fun getStudentParticipationOfLessons(studentID: Long): Flow<List<ParticipantLessonModel>>

    @[Transaction Query(FETCH_BY_STUDENT_AND_TEACHER_ID_QUERY)]
    fun getStudentParticipationOfTeacherLessons(
        studentID: Long,
        teacherID: Long
    ): Flow<List<ParticipantLessonModel>>

    @Query(FETCH_BY_LESSON_ID_QUERY)
    suspend fun getEntities(lessonID: Long): List<ParticipantEntity>

    @Update(entity = ParticipantEntity::class)
    suspend fun updateData(vararg participantUpdatableModel: ParticipantUpdatableModel)

    @Update(entity = ParticipantEntity::class)
    suspend fun updateActivity(vararg participantActivityModel: ParticipantActivityModel)


    private companion object {
        const val FETCH_BY_LESSON_ID_QUERY = """
            SELECT * FROM participant WHERE lesson_id = :lessonID
        """

        const val FETCH_BY_STUDENT_ID_QUERY = """
            SELECT * FROM participant WHERE student_id = :studentID
        """

        const val FETCH_BY_STUDENT_AND_TEACHER_ID_QUERY = """
            SELECT p.* FROM participant AS p INNER JOIN lesson AS l ON p.lesson_id = l.id
            WHERE p.student_id = :studentID AND l.teacher_id = :teacherID
        """
    }
}
