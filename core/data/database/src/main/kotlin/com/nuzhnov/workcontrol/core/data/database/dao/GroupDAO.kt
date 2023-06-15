package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.GroupEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class GroupDAO : EntityDAO<GroupEntity>(entityName = "student_group") {
    @Query("SELECT * FROM student_group WHERE department_id = :departmentID")
    abstract fun getEntitiesFlow(departmentID: Long): Flow<List<GroupEntity>>
}
