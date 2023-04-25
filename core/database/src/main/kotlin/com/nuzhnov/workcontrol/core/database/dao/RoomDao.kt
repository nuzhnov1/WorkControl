package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.RoomEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
interface RoomDao : BaseDao<RoomEntity> {
    @Query(FETCH_QUERY)
    override fun getEntitiesFlow(): Flow<List<RoomEntity>>

    @Query(FETCH_BY_BUILDING_ID_QUERY)
    fun getBuildingRoomsFlow(buildingID: Long): Flow<List<RoomEntity>>

    @Query(FETCH_QUERY)
    override suspend fun getEntities(): List<RoomEntity>

    @Query(FETCH_BY_BUILDING_ID_QUERY)
    suspend fun getBuildingRooms(buildingID: Long): List<RoomEntity>


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM room"
        const val FETCH_BY_BUILDING_ID_QUERY = "SELECT * FROM room WHERE building_id = :buildingID"
    }
}
