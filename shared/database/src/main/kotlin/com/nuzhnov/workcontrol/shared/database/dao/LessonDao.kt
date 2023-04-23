package com.nuzhnov.workcontrol.shared.database.dao

import com.nuzhnov.workcontrol.shared.database.entity.LessonEntity
import com.nuzhnov.workcontrol.shared.database.models.LessonEntityModel
import com.nuzhnov.workcontrol.shared.models.Lesson
import kotlinx.coroutines.flow.Flow
import androidx.room.*

@Dao
interface LessonDao : BaseDao<LessonEntity> {
    @Query(FETCH_QUERY)
    override fun getEntitiesFlow(): Flow<List<LessonEntity>>

    @[Transaction Query(FETCH_BY_TEACHER_ID_AND_STATE_QUERY)]
    fun getTeacherLessonsFlow(
        teacherID: Long,
        state: Lesson.State
    ): Flow<List<LessonEntityModel>>

    @[Transaction Query(FETCH_BY_TEACHER_ID_STATE_AND_DISCIPLINE_ID_QUERY)]
    fun getTeacherDisciplineLessonsFlow(
        teacherID: Long,
        state: Lesson.State,
        disciplineID: Long
    ): Flow<List<LessonEntityModel>>

    @Query(FETCH_QUERY)
    override suspend fun getEntities(): List<LessonEntity>

    @[Transaction Query(FETCH_BY_TEACHER_ID_AND_STATE_QUERY)]
    suspend fun getTeacherLessons(
        teacherID: Long,
        state: Lesson.State
    ): List<LessonEntityModel>

    @[Transaction Query(FETCH_BY_TEACHER_ID_STATE_AND_DISCIPLINE_ID_QUERY)]
    suspend fun getTeacherDisciplineLessons(
        teacherID: Long,
        state: Lesson.State,
        disciplineID: Long
    ): List<LessonEntityModel>

    @Query(CLEAR_QUERY)
    override suspend fun clear()


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM lesson"

        const val FETCH_BY_TEACHER_ID_AND_STATE_QUERY = """
            SELECT * FROM lesson
            WHERE teacher_id = :teacherID AND state = :state"
        """

        const val FETCH_BY_TEACHER_ID_STATE_AND_DISCIPLINE_ID_QUERY = """
            SELECT * FROM lesson
            WHERE teacher_id = :teacherID AND state = :state AND discipline_id = :disciplineID
        """

        const val CLEAR_QUERY = "DELETE FROM lesson"
    }
}
