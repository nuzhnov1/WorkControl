package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.DepartmentStatisticsEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Query

interface DepartmentStatisticsDAO : BaseDAO<DepartmentStatisticsEntity> {
    @Query(FETCH_BY_DEPARTMENT_ID_QUERY)
    fun getEntityFlow(departmentID: Long): Flow<DepartmentStatisticsEntity?>


    private companion object {
        const val FETCH_BY_DEPARTMENT_ID_QUERY = """
            SELECT * FROM department_statistics WHERE department_id = :departmentID 
        """
    }
}
