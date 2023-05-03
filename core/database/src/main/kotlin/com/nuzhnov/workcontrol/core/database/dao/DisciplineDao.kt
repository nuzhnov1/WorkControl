package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.DisciplineEntity
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Query

@Dao
interface DisciplineDao : BaseDao<DisciplineEntity> {
    @Query(FETCH_QUERY)
    suspend fun getEntities(): List<DisciplineEntity>

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
        const val FETCH_QUERY = "SELECT * FROM discipline"
    }
}
