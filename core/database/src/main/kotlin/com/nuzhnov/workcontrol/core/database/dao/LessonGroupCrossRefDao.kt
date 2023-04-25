package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.LessonGroupCrossRefEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
interface LessonGroupCrossRefDao : BaseDao<LessonGroupCrossRefEntity> {
    @Query(FETCH_QUERY)
    override fun getEntitiesFlow(): Flow<List<LessonGroupCrossRefEntity>>

    @Query(FETCH_QUERY)
    override suspend fun getEntities(): List<LessonGroupCrossRefEntity>


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM lesson_student_group_cross_ref"
    }
}
