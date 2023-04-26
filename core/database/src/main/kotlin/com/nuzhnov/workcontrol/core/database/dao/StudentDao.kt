package com.nuzhnov.workcontrol.core.database.dao

import android.database.sqlite.SQLiteConstraintException
import com.nuzhnov.workcontrol.core.database.entity.StudentEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
interface StudentDao : BaseDao<StudentEntity> {
    @Query(FETCH_QUERY)
    fun getEntitiesFlow(): Flow<List<StudentEntity>>

    @Query(FETCH_BY_GROUP_ID_QUERY)
    fun getGroupStudentsFlow(studentGroupID: Long): Flow<List<StudentEntity>>

    @Query(FETCH_QUERY)
    suspend fun getEntities(): List<StudentEntity>

    @Query(FETCH_BY_GROUP_ID_QUERY)
    suspend fun getGroupStudents(studentGroupID: Long): List<StudentEntity>

    suspend fun clear() = getEntities().forEach { entity ->
        runCatching { delete(entity) }.onFailure { cause ->
            if (cause !is SQLiteConstraintException) {
                throw cause
            }
        }
    }


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM student"
        const val FETCH_BY_GROUP_ID_QUERY = "SELECT * FROM student WHERE student_group_id = :studentGroupID"
    }
}
