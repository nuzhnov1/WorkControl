package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.DisciplineEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
interface DisciplineDao : BaseDao<DisciplineEntity> {
    @Query(FETCH_QUERY)
    override suspend fun getEntities(): List<DisciplineEntity>

    @Query(FETCH_BY_TEACHER_ID_QUERY)
    fun getTeacherDisciplinesFlow(teacherID: Long): Flow<List<DisciplineEntity>>

    @Query(FETCH_QUERY)
    override fun getEntitiesFlow(): Flow<List<DisciplineEntity>>

    @Query(FETCH_BY_TEACHER_ID_QUERY)
    suspend fun getTeacherDisciplines(teacherID: Long): List<DisciplineEntity>


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM discipline"

        const val FETCH_BY_TEACHER_ID_QUERY = """
            SELECT d.id, d.name FROM discipline AS d
                INNER JOIN teacher_discipline_cross_ref AS ref ON d.id = ref.discipline_id
                INNER JOIN teacher AS t ON ref.teacher_id = t.id
            WHERE teacher.id = :teacherID
        """
    }
}
