package com.nuzhnov.workcontrol.shared.database.dao

import com.nuzhnov.workcontrol.shared.database.entity.FacultyEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
interface FacultyDao : BaseDao<FacultyEntity> {
    @Query(FETCH_QUERY)
    override fun getEntitiesFlow(): Flow<List<FacultyEntity>>

    @Query(FETCH_QUERY)
    override suspend fun getEntities(): List<FacultyEntity>


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM faculty"
    }
}
