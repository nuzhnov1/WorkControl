package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.DepartmentEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class DepartmentDAO : EntityDAO<DepartmentEntity>(entityName = "department") {
    @Query("SELECT * FROM department")
    abstract fun getEntitiesFlow(): Flow<List<DepartmentEntity>>
}
