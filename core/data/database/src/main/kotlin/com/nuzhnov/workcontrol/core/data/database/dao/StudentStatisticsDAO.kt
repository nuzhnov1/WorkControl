package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.StudentStatisticsEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class StudentStatisticsDAO :
    EntityDAO<StudentStatisticsEntity>(entityName = "student_statistics") {

    @Query("SELECT * FROM student_statistics WHERE student_id = :studentID")
    abstract fun getEntityFlow(studentID: Long): Flow<StudentStatisticsEntity?>
}
