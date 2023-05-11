package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.LessonEntity
import com.nuzhnov.workcontrol.core.database.entity.model.LessonModel
import com.nuzhnov.workcontrol.core.database.entity.model.LessonWithParticipantEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.*

@Dao
interface LessonDAO : BaseDAO<LessonEntity> {
    @[Transaction Query(FETCH_CREATED_LESSONS_QUERY)]
    fun getCreatedLessonsFlow(): Flow<List<LessonModel>>

    @[Transaction Query(FETCH_ACTIVE_LESSON_QUERY)]
    suspend fun getActiveLesson(): LessonModel?

    @[Transaction Query(FETCH_ACTIVE_LESSON_QUERY)]
    fun getActiveLessonFlow(): Flow<LessonModel?>

    @[Transaction Query(FETCH_FINISHED_LESSONS_QUERY)]
    fun getFinishedLessonFlow(): Flow<List<LessonModel>>

    @[Transaction Query(FETCH_CREATED_LESSONS_BY_DISCIPLINE_ID_QUERY)]
    fun getCreatedDisciplineLessonsFlow(disciplineID: Long): Flow<List<LessonModel>>

    @[Transaction Query(FETCH_FINISHED_LESSONS_BY_DISCIPLINE_ID_QUERY)]
    fun getDisciplineFinishedLessonsFlow(disciplineID: Long): Flow<List<LessonModel>>

    @Query(FETCH_FINISHED_LESSONS_QUERY)
    suspend fun getFinishedLessonEntities(): List<LessonEntity>

    @[Transaction Query(FETCH_FINISHED_LESSONS_QUERY)]
    suspend fun getFinishedEntitiesWithParticipants(): List<LessonWithParticipantEntity>

    @Query(SET_STATE_TO_ACTIVE_BY_ID_QUERY)
    suspend fun startLesson(lessonID: Long)

    @Query(SET_STATE_TO_FINISHED_BY_ID_QUERY)
    suspend fun finishLesson(lessonID: Long)


    private companion object {
        const val FETCH_CREATED_LESSONS_QUERY = "SELECT * FROM lesson WHERE state = 'CREATED'"

        const val FETCH_ACTIVE_LESSON_QUERY = "SELECT * FROM lesson WHERE state = 'ACTIVE'"

        const val FETCH_FINISHED_LESSONS_QUERY = "SELECT * FROM lesson WHERE state = 'FINISHED'"

        const val FETCH_CREATED_LESSONS_BY_DISCIPLINE_ID_QUERY = """
            SELECT * FROM lesson
            WHERE state = 'CREATED' AND discipline_id = :disciplineID
        """

        const val FETCH_FINISHED_LESSONS_BY_DISCIPLINE_ID_QUERY = """
            SELECT * FROM lesson
            WHERE state = 'FINISHED' AND discipline_id = :disciplineID
        """

        const val SET_STATE_TO_ACTIVE_BY_ID_QUERY = """
            UPDATE lesson SET state = 'ACTIVE' WHERE id = :lessonID
        """

        const val SET_STATE_TO_FINISHED_BY_ID_QUERY = """
            UPDATE lesson SET state = 'FINISHED' WHERE id = :lessonID
        """
    }
}
