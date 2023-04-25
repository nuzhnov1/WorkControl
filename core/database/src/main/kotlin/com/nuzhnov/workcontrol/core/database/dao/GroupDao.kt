package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.GroupEntity
import com.nuzhnov.workcontrol.core.database.models.GroupWithFaculty
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface GroupDao : BaseDao<GroupEntity> {
    @Query(FETCH_QUERY)
    override fun getEntitiesFlow(): Flow<List<GroupEntity>>

    @Query(FETCH_BY_FACULTY_ID_QUERY)
    fun getFacultyGroupsFlow(facultyID: Long): Flow<List<GroupEntity>>

    @Query(FETCH_QUERY)
    override suspend fun getEntities(): List<GroupEntity>

    @Query(FETCH_BY_FACULTY_ID_QUERY)
    suspend fun getFacultyGroups(facultyID: Long): List<GroupEntity>

    @[Transaction Query(FETCH_BY_GROUP_ID_QUERY)]
    suspend fun getGroupWithFaculty(groupID: Long): GroupWithFaculty


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM student_group"
        const val FETCH_BY_FACULTY_ID_QUERY = "SELECT * FROM student_group WHERE faculty_id = :facultyID"
        const val FETCH_BY_GROUP_ID_QUERY = "SELECT * FROM student_group WHERE id = :groupID"
    }
}
