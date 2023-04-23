package com.nuzhnov.workcontrol.shared.database.dao

import com.nuzhnov.workcontrol.shared.database.entity.BuildingEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
interface BuildingDao : BaseDao<BuildingEntity> {
    @Query(FETCH_QUERY)
    override fun getEntitiesFlow(): Flow<List<BuildingEntity>>

    @Query(FETCH_QUERY)
    override suspend fun getEntities(): List<BuildingEntity>


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM building"
    }
}
