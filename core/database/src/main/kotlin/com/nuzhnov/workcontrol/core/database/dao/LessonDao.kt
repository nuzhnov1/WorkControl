package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.LessonEntity
import com.nuzhnov.workcontrol.core.database.entity.model.LessonModel
import com.nuzhnov.workcontrol.core.model.Lesson.State
import kotlinx.coroutines.flow.Flow
import androidx.room.*

@Dao
interface LessonDao : BaseDao<LessonEntity> {
    @[Transaction Query(FETCH_BY_TEACHER_ID_AND_STATE_QUERY)]
    fun getTeacherLessonsFlow(
        teacherID: Long,
        state: State
    ): Flow<List<LessonModel>>

    @[Transaction Query(FETCH_BY_TEACHER_ID_STATE_AND_DISCIPLINE_ID_QUERY)]
    fun getTeacherDisciplineLessonsFlow(
        teacherID: Long,
        disciplineID: Long,
        state: State
    ): Flow<List<LessonModel>>

    @Query(FETCH_BY_STATE_QUERY)
    suspend fun getLessons(state: State)

    @Query(CLEAR_SYNCHRONIZED_BY_STATE_QUERY)
    suspend fun clearSynchronizedByState(state: State)


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

        const val CLEAR_SYNCHRONIZED_BY_STATE_QUERY = """
            DELETE FROM lesson WHERE state = :state AND is_synchronized = true 
        """
    }
}
