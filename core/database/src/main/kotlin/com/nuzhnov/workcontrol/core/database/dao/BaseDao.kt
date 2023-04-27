package com.nuzhnov.workcontrol.core.database.dao

import androidx.room.Upsert
import androidx.room.Delete

interface BaseDao<T> {
    @Upsert
    suspend fun insertOrUpdate(vararg entity: T)

    @Delete
    suspend fun delete(vararg entity: T)
}