package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.GroupStatisticsEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Query

interface GroupStatisticsDAO : BaseDAO<GroupStatisticsEntity> {
    @Query(FETCH_BY_GROUP_ID_QUERY)
    fun getEntityFlow(groupID: Long): Flow<GroupStatisticsEntity?>


    private companion object {
        const val FETCH_BY_GROUP_ID_QUERY = """
            SELECT * FROM group_statistics WHERE group_id = :groupID 
        """
    }
}
