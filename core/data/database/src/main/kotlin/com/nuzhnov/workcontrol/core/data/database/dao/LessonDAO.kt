package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.LessonEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.LessonModel
import com.nuzhnov.workcontrol.core.data.database.entity.model.LessonWithParticipantEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.*

@Dao
interface LessonDAO : BaseDAO<LessonEntity> {
    @Query(FETCH_BY_ID_QUERY)
    suspend fun getEntity(lessonID: Long): LessonEntity?

    @[Transaction Query(FETCH_SCHEDULED_LESSONS_BY_TEACHER_ID_QUERY)]
    fun getTeacherScheduledLessonsFlow(teacherID: Long): Flow<List<LessonModel>>

    @[Transaction Query(FETCH_ACTIVE_LESSON_BY_TEACHER_ID_QUERY)]
    suspend fun getTeacherActiveLesson(teacherID: Long): LessonModel?

    @[Transaction Query(FETCH_ACTIVE_LESSON_BY_TEACHER_ID_QUERY)]
    fun getTeacherActiveLessonFlow(teacherID: Long): Flow<LessonModel?>

    @[Transaction Query(FETCH_FINISHED_LESSONS_BY_TEACHER_ID_QUERY)]
    fun getTeacherFinishedLessonFlow(teacherID: Long): Flow<List<LessonModel>>

    @[Transaction Query(FETCH_SCHEDULED_LESSONS_BY_TEACHER_ID_AND_DISCIPLINE_ID_QUERY)]
    fun getTeacherScheduledDisciplineLessonsFlow(
        teacherID: Long,
        disciplineID: Long
    ): Flow<List<LessonModel>>

    @[Transaction Query(FETCH_FINISHED_LESSONS_BY_TEACHER_ID_AND_DISCIPLINE_ID_QUERY)]
    fun getTeacherFinishedDisciplineLessonsFlow(
        teacherID: Long,
        disciplineID: Long
    ): Flow<List<LessonModel>>

    @Query(FETCH_FINISHED_LESSON_QUERY)
    suspend fun getFinishedLessonEntities(): List<LessonEntity>

    @[Transaction Query(FETCH_FINISHED_LESSON_QUERY)]
    suspend fun getFinishedEntitiesWithParticipants(): List<LessonWithParticipantEntity>


    private companion object {
        const val FETCH_BY_ID_QUERY = "SELECT * FROM lesson WHERE id = :lessonID"

        const val FETCH_SCHEDULED_LESSONS_BY_TEACHER_ID_QUERY = """
            SELECT * FROM lesson WHERE state = 'SCHEDULED' AND teacher_id = :teacherID
        """

        const val FETCH_ACTIVE_LESSON_BY_TEACHER_ID_QUERY = """
            SELECT * FROM lesson WHERE state = 'ACTIVE' AND teacher_id = :teacherID
        """

        const val FETCH_FINISHED_LESSONS_BY_TEACHER_ID_QUERY = """
            SELECT * FROM lesson WHERE state = 'FINISHED' AND teacher_id = :teacherID
        """

        const val FETCH_SCHEDULED_LESSONS_BY_TEACHER_ID_AND_DISCIPLINE_ID_QUERY = """
            SELECT * FROM lesson
            WHERE state = 'SCHEDULED' AND teacher_id = :teacherID AND discipline_id = :disciplineID
        """

        const val FETCH_FINISHED_LESSONS_BY_TEACHER_ID_AND_DISCIPLINE_ID_QUERY = """
            SELECT * FROM lesson
            WHERE state = 'FINISHED' AND teacher_id = :teacherID AND discipline_id = :disciplineID
        """

        const val FETCH_FINISHED_LESSON_QUERY = "SELECT * FROM lesson WHERE state = 'FINISHED'"
    }
}
