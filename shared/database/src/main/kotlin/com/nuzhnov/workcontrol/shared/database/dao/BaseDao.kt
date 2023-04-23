package com.nuzhnov.workcontrol.shared.database.dao

import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Upsert
import androidx.room.Delete

interface BaseDao<T> {
    fun getEntitiesFlow(): Flow<List<T>>

    suspend fun getEntities(): List<T>

    @Upsert
    suspend fun insertOrUpdate(vararg entity: T)

    @Delete
    suspend fun delete(vararg entity: T)

    suspend fun clear() = getEntities().forEach { entity ->
        runCatching { delete(entity) }.onFailure { cause ->
            if (cause !is SQLiteConstraintException) {
                throw cause
            }
        }
    }
}
