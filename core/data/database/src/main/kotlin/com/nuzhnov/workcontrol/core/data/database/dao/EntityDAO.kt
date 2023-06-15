package com.nuzhnov.workcontrol.core.data.database.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Upsert
import androidx.room.Delete
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

abstract class EntityDAO<T>(val entityName: String) {
    @RawQuery
    abstract suspend fun getEntities(query: SupportSQLiteQuery): List<T>

    suspend fun getEntities() = getEntities(SimpleSQLiteQuery(query = "SELECT * FROM $entityName"))

    @Upsert
    abstract suspend fun insertOrUpdate(vararg entity: T)

    @Delete
    abstract suspend fun delete(vararg entity: T)

    suspend fun clear() = getEntities().forEach { entity ->
        runCatching { delete(entity) }.onFailure { cause ->
            if (cause !is SQLiteConstraintException) {
                throw cause
            }
        }
    }
}
