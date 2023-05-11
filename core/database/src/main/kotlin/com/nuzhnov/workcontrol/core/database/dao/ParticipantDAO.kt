package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.database.entity.model.ParticipantModel
import com.nuzhnov.workcontrol.core.database.entity.model.update.ParticipantUpdatableModel
import com.nuzhnov.workcontrol.core.database.entity.model.update.ParticipantActivityModel
import kotlinx.coroutines.flow.Flow
import androidx.room.*

@Dao
interface ParticipantDAO : BaseDAO<ParticipantEntity> {
    @[Transaction Query(FETCH_BY_LESSON_ID_QUERY)]
    fun getParticipantsFlow(lessonID: Long): Flow<List<ParticipantModel>>

    @Query(FETCH_BY_LESSON_ID_QUERY)
    suspend fun getEntities(lessonID: Long): List<ParticipantEntity>

    @Query(FETCH_BY_LESSON_ID_LIST_QUERY)
    suspend fun getEntities(lessonIDList: List<Long>): List<ParticipantEntity>

    @Update(entity = ParticipantEntity::class)
    suspend fun updateData(vararg participantUpdatableModel: ParticipantUpdatableModel)

    @Update(entity = ParticipantEntity::class)
    suspend fun updateActivity(vararg participantActivityModel: ParticipantActivityModel)


    private companion object {
        const val FETCH_BY_LESSON_ID_QUERY = """
            SELECT * FROM participant WHERE lesson_id = :lessonID
        """

        const val FETCH_BY_LESSON_ID_LIST_QUERY = """
            SELECT * FROM participant WHERE lesson_id IN (:lessonIDList)
        """
    }
}
