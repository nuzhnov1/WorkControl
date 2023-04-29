package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.StudentEntity
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Query

@Dao
interface StudentDao : BaseDao<StudentEntity> {
    @Query(FETCH_QUERY)
    suspend fun getEntities(): List<StudentEntity>

    @Query(FETCH_BY_STUDENT_GROUP_ID_QUERY)
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
        const val FETCH_BY_STUDENT_GROUP_ID_QUERY = "SELECT * FROM student WHERE id = :studentGroupID"
    }
}
