package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.StudentEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.StudentModel
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface StudentDAO : BaseDAO<StudentEntity> {
    @[Transaction Query(FETCH_BY_ID_QUERY)]
    suspend fun getStudent(id: Long): StudentModel?

    @[Transaction Query(FETCH_BY_ID_QUERY)]
    fun getStudentFlow(id: Long): Flow<StudentModel?>

    @Query(FETCH_QUERY)
    suspend fun getEntities(): List<StudentEntity>

    @Query(FETCH_BY_GROUP_ID_QUERY)
    fun getEntitiesFlow(groupID: Long): Flow<List<StudentEntity>>

    suspend fun clear(vararg exceptionID: Long): Unit = getEntities()
        .filterNot { entity -> entity.id in exceptionID }
        .forEach { entity ->
            runCatching { delete(entity) }.onFailure { cause ->
                if (cause !is SQLiteConstraintException) {
                    throw cause
                }
            }
        }


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM student"

        const val FETCH_BY_ID_QUERY = "SELECT * FROM student WHERE id = :id"

        const val FETCH_BY_GROUP_ID_QUERY = "SELECT * FROM student WHERE group_id = :groupID"
    }
}
