package com.nuzhnov.workcontrol.core.data.database.dao

import androidx.room.Upsert
import androidx.room.Delete

interface BaseDAO<T> {
    @Upsert
    suspend fun insertOrUpdate(vararg entity: T)

    @Delete
    suspend fun delete(vararg entity: T)
}
