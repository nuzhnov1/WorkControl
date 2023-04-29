package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.LessonGroupCrossRefEntity
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Query

@Dao
interface LessonStudentGroupCrossRefDao : BaseDao<LessonGroupCrossRefEntity> {
    @Query(FETCH_QUERY)
    suspend fun getEntities(): List<LessonGroupCrossRefEntity>

    suspend fun clear() = getEntities().forEach { entity ->
        runCatching { delete(entity) }.onFailure { cause ->
            if (cause !is SQLiteConstraintException) {
                throw cause
            }
        }
    }


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM lesson_student_group_cross_ref"
    }
}
