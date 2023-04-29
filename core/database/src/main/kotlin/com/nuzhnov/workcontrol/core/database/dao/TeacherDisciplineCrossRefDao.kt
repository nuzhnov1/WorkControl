package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.TeacherDisciplineCrossRefEntity
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Query

@Dao
interface TeacherDisciplineCrossRefDao : BaseDao<TeacherDisciplineCrossRefEntity> {
    @Query(FETCH_QUERY)
    suspend fun getEntities(): List<TeacherDisciplineCrossRefEntity>

    suspend fun clear() = getEntities().forEach { entity ->
        runCatching { delete(entity) }.onFailure { cause ->
            if (cause !is SQLiteConstraintException) {
                throw cause
            }
        }
    }


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM teacher_discipline_cross_ref"
    }
}
