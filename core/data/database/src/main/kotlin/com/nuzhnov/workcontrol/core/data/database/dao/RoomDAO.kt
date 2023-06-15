package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.RoomEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class RoomDAO : EntityDAO<RoomEntity>(entityName = "room") {
    @Query("SELECT * FROM room WHERE building_id = :buildingID")
    abstract fun getEntitiesFlow(buildingID: Long): Flow<List<RoomEntity>>
}
