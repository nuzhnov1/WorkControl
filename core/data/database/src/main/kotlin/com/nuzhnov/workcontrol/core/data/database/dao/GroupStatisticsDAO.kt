package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.GroupStatisticsEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class GroupStatisticsDAO :
    EntityDAO<GroupStatisticsEntity>(entityName = "student_group_statistics") {

    @Query("SELECT * FROM student_group_statistics WHERE group_id = :groupID")
    abstract fun getEntityFlow(groupID: Long): Flow<GroupStatisticsEntity?>
}
