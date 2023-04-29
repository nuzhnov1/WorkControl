package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.LessonEntity
import com.nuzhnov.workcontrol.core.database.model.LessonEntityModel
import com.nuzhnov.workcontrol.core.models.Lesson
import androidx.room.*

@Dao
interface LessonDao : BaseDao<LessonEntity> {
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
    suspend fun clear()


    private companion object {
        const val FETCH_BY_TEACHER_ID_AND_STATE_QUERY = """
            SELECT * FROM lesson
            WHERE teacher_id = :teacherID AND state = :state
        """

        const val FETCH_BY_TEACHER_ID_STATE_AND_DISCIPLINE_ID_QUERY = """
            SELECT * FROM lesson
            WHERE teacher_id = :teacherID AND state = :state AND discipline_id = :disciplineID
        """

        const val CLEAR_QUERY = "DELETE FROM lesson"
    }
}
