package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.StudentEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
interface StudentDao : BaseDao<StudentEntity> {
    @Query(FETCH_QUERY)
    override fun getEntitiesFlow(): Flow<List<StudentEntity>>

    @Query(FETCH_BY_GROUP_ID_QUERY)
    fun getGroupStudentsFlow(groupID: Long): Flow<List<StudentEntity>>

    @Query(FETCH_QUERY)
    override suspend fun getEntities(): List<StudentEntity>

    @Query(FETCH_BY_GROUP_ID_QUERY)
    suspend fun getGroupStudents(groupID: Long): List<StudentEntity>


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM student"
        const val FETCH_BY_GROUP_ID_QUERY = "SELECT * FROM student WHERE student_group_id = :groupID"
    }
}
