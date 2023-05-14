package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.RoomEntity
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Query

@Dao
interface RoomDAO : BaseDAO<RoomEntity> {
    @Query(FETCH_QUERY)
    suspend fun getEntities(): List<RoomEntity>

    @Query(FETCH_BY_BUILDING_ID_QUERY)
    fun getEntitiesFlow(buildingID: Long): Flow<List<RoomEntity>>

    suspend fun clear(vararg exceptionID: Long): Unit = getEntities()
        .filterNot { entity -> entity.id in exceptionID }
        .forEach { entity ->
            runCatching { delete(entity) }.onFailure { cause ->
                if (cause !is SQLiteConstraintException) {
                    throw cause
                }
            }
        }


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM room"

        const val FETCH_BY_BUILDING_ID_QUERY = """
            SELECT * FROM room WHERE building_id = :buildingID 
        """
    }
}
