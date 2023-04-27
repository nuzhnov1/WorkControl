package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.BuildingEntity
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Query

@Dao
interface BuildingDao : BaseDao<BuildingEntity> {
    @Query(FETCH_QUERY)
    fun getEntitiesFlow(): Flow<List<BuildingEntity>>

    @Query(FETCH_QUERY)
    suspend fun getEntities(): List<BuildingEntity>

    suspend fun clear() = getEntities().forEach { entity ->
        runCatching { delete(entity) }.onFailure { cause ->
            if (cause !is SQLiteConstraintException) {
                throw cause
            }
        }
    }


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM building"
    }
}