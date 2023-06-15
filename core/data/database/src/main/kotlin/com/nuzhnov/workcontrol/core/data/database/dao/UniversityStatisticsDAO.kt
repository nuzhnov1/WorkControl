package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.UniversityStatisticsEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Query

interface UniversityStatisticsDAO : BaseDAO<UniversityStatisticsEntity> {
    @Query(FETCH_QUERY)
    fun getEntityFlow(): Flow<UniversityStatisticsEntity?>


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM department_statistics WHERE id = 0"
    }
}
