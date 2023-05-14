package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.GroupEntity
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Query

@Dao
interface GroupDAO : BaseDAO<GroupEntity> {
    @Query(FETCH_QUERY)
    suspend fun getEntities(): List<GroupEntity>

    @Query(FETCH_BY_FACULTY_ID_QUERY)
    fun getEntitiesFlow(facultyID: Long): Flow<List<GroupEntity>>

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
        const val FETCH_QUERY = "SELECT * FROM student_group"

        const val FETCH_BY_FACULTY_ID_QUERY = """
            SELECT * FROM student_group WHERE faculty_id = :facultyID
        """
    }
}
