package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.TeacherEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.TeacherModel
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface TeacherDAO : BaseDAO<TeacherEntity> {
    @[Transaction Query(FETCH_BY_ID_QUERY)]
    suspend fun getTeacher(id: Long): TeacherModel?

    @[Transaction Query(FETCH_BY_ID_QUERY)]
    fun getTeacherFlow(id: Long): Flow<TeacherModel?>

    @Query(FETCH_QUERY)
    suspend fun getEntities(): List<TeacherEntity>

    suspend fun clear(vararg exceptionID: Long) = getEntities()
        .filterNot { entity -> entity.id in exceptionID }
        .forEach { entity ->
            runCatching { delete(entity) }.onFailure { cause ->
                if (cause !is SQLiteConstraintException) {
                    throw cause
                }
            }
        }


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM teacher"
        const val FETCH_BY_ID_QUERY = "SELECT * FROM teacher WHERE id = :id"
    }
}
