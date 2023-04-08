package com.nuzhnov.workcontrol.shared.visitservice.data.local.dao

import com.nuzhnov.workcontrol.shared.visitservice.data.local.entity.VisitorEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.*

@Dao
internal interface VisitorsDao {
    @Query("SELECT * FROM visitor")
    suspend fun getVisitors(): Set<VisitorEntity>

    @Query("SELECT * FROM visitor")
    fun getVisitorsFlow(): Flow<Set<VisitorEntity>>

    @Upsert
    suspend fun insertOrUpdate(vararg visitorEntity: VisitorEntity)


    @Query("DELETE FROM visitor")
    suspend fun clearVisitors()
}
