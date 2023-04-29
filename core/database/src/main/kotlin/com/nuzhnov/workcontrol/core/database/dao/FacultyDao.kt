package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.FacultyEntity
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Query

@Dao
interface FacultyDao : BaseDao<FacultyEntity> {
    @Query(FETCH_QUERY)
    suspend fun getEntities(): List<FacultyEntity>

    suspend fun clear() = getEntities().forEach { entity ->
        runCatching { delete(entity) }.onFailure { cause ->
            if (cause !is SQLiteConstraintException) {
                throw cause
            }
        }
    }


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM faculty"
    }
}
