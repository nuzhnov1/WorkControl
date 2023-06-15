package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.UniversityStatisticsEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class UniversityStatisticsDAO :
    EntityDAO<UniversityStatisticsEntity>(entityName = "university_statistics") {

    @Query("SELECT * FROM university_statistics WHERE id = 0")
    abstract fun getEntityFlow(): Flow<UniversityStatisticsEntity?>
}
