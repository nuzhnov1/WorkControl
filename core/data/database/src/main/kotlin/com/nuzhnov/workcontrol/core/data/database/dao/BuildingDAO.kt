package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.BuildingEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class BuildingDAO : EntityDAO<BuildingEntity>(entityName = "building") {
    @Query("SELECT * FROM building")
    abstract fun getEntitiesFlow(): Flow<List<BuildingEntity>>
}
