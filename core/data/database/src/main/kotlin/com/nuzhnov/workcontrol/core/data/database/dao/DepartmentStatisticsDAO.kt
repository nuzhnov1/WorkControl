package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.DepartmentStatisticsEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class DepartmentStatisticsDAO :
    EntityDAO<DepartmentStatisticsEntity>(entityName = "department_statistics") {

    @Query("SELECT * FROM department_statistics WHERE department_id = :departmentID ")
    abstract fun getEntityFlow(departmentID: Long): Flow<DepartmentStatisticsEntity?>
}
