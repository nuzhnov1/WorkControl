package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.RoomEntity
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Query

@Dao
interface RoomDao : BaseDao<RoomEntity> {
    @Query(FETCH_QUERY)
    suspend fun getEntities(): List<RoomEntity>

    @Query(FETCH_BY_BUILDING_ID_QUERY)
    suspend fun getBuildingRooms(buildingID: Long): List<RoomEntity>

    suspend fun clear() = getEntities().forEach { entity ->
        runCatching { delete(entity) }.onFailure { cause ->
            if (cause !is SQLiteConstraintException) {
                throw cause
            }
        }
    }


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM room"
        const val FETCH_BY_BUILDING_ID_QUERY = "SELECT * FROM room WHERE building_id = :buildingID"
    }
}
