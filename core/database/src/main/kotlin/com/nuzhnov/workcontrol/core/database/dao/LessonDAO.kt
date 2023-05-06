package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.model.LessonWithParticipantEntity
import com.nuzhnov.workcontrol.core.database.entity.LessonEntity
import com.nuzhnov.workcontrol.core.database.entity.model.LessonModel
import com.nuzhnov.workcontrol.core.model.Lesson.State
import kotlinx.coroutines.flow.Flow
import androidx.room.*

@Dao
interface LessonDAO : BaseDAO<LessonEntity> {
    @[Transaction Query(FETCH_BY_TEACHER_ID_AND_STATE_QUERY)]
    fun getLessonsFlow(
        teacherID: Long,
        state: State
    ): Flow<List<LessonModel>>

    @[Transaction Query(FETCH_BY_TEACHER_ID_STATE_AND_DISCIPLINE_ID_QUERY)]
    fun getDisciplineLessonsFlow(
        teacherID: Long,
        disciplineID: Long,
        state: State
    ): Flow<List<LessonModel>>

    @Query(FETCH_BY_STATE_QUERY)
    suspend fun getEntities(state: State): List<LessonEntity>

    @[Transaction Query(FETCH_BY_STATE_QUERY)]
    suspend fun getEntitiesWithParticipants(state: State): List<LessonWithParticipantEntity>


    private companion object {
        const val FETCH_BY_STATE_QUERY = "SELECT * FROM lesson WHERE state = :state"

        const val FETCH_BY_TEACHER_ID_AND_STATE_QUERY = """
            SELECT * FROM lesson
            WHERE teacher_id = :teacherID AND state = :state
        """

        const val FETCH_BY_TEACHER_ID_STATE_AND_DISCIPLINE_ID_QUERY = """
            SELECT * FROM lesson
            WHERE teacher_id = :teacherID AND state = :state AND discipline_id = :disciplineID
        """
    }
}
