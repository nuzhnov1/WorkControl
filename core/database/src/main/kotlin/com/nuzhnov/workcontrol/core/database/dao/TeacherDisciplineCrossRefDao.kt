package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.TeacherDisciplineCrossRefEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
interface TeacherDisciplineCrossRefDao : BaseDao<TeacherDisciplineCrossRefEntity> {
    @Query(FETCH_QUERY)
    override fun getEntitiesFlow(): Flow<List<TeacherDisciplineCrossRefEntity>>

    @Query(FETCH_QUERY)
    override suspend fun getEntities(): List<TeacherDisciplineCrossRefEntity>


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM teacher_discipline_cross_ref"
    }
}
