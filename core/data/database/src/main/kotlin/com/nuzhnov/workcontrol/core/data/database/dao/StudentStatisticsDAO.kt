package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.StudentStatisticsEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Query

interface StudentStatisticsDAO : BaseDAO<StudentStatisticsEntity> {
    @Query(FETCH_BY_STUDENT_ID_QUERY)
    fun getEntityFlow(studentID: Long): Flow<StudentStatisticsEntity?>


    private companion object {
        const val FETCH_BY_STUDENT_ID_QUERY = """
            SELECT * FROM student_statistics WHERE student_id = :studentID 
        """
    }
}
