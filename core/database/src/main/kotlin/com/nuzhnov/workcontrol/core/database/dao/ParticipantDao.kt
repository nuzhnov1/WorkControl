package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.database.model.ParticipantEntityModel
import com.nuzhnov.workcontrol.core.database.model.ParticipantWithLesson
import com.nuzhnov.workcontrol.core.database.model.ParticipantUpdatableData
import com.nuzhnov.workcontrol.core.database.model.ParticipantActivity
import kotlinx.coroutines.flow.Flow
import androidx.room.*

@Dao
interface ParticipantDao : BaseDao<ParticipantEntity> {
    @[Transaction Query(FETCH_BY_LESSON_ID_QUERY)]
    fun getParticipantsOfLessonFlow(lessonID: Long): Flow<List<ParticipantEntityModel>>

    @Query(FETCH_QUERY)
    suspend fun getEntities(): List<ParticipantEntity>

    @[Transaction Query(FETCH_BY_LESSON_ID_QUERY)]
    suspend fun getParticipantsOfLesson(lessonID: Long): List<ParticipantEntityModel>

    @[Transaction Query(FETCH_BY_STUDENT_ID_QUERY)]
    suspend fun getParticipantWithLessons(studentID: Long): List<ParticipantWithLesson>

    @Update(entity = ParticipantEntity::class)
    suspend fun updateData(vararg participantUpdatableData: ParticipantUpdatableData)

    @Update(entity = ParticipantEntity::class)
    suspend fun updateActivity(vararg participantActivity: ParticipantActivity)

    @Query(CLEAR_QUERY)
    suspend fun clear()


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM participant"

        const val FETCH_BY_LESSON_ID_QUERY = """
            SELECT * FROM participant WHERE lesson_id = :lessonID
        """

        const val FETCH_BY_STUDENT_ID_QUERY = """
            SELECT * FROM participant WHERE student_id = :studentID
        """

        const val CLEAR_QUERY = "DELETE FROM participant"
    }
}
