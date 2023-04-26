package com.nuzhnov.workcontrol.core.database.dao

import android.database.sqlite.SQLiteConstraintException
import com.nuzhnov.workcontrol.core.database.entity.FacultyEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
interface FacultyDao : BaseDao<FacultyEntity> {
    @Query(FETCH_QUERY)
    fun getEntitiesFlow(): Flow<List<FacultyEntity>>

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
