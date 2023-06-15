package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.StudentEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.StudentEntityModel
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class StudentDAO : EntityDAO<StudentEntity>(entityName = "student") {
    @Transaction
    @Query("SELECT * FROM student WHERE id = :id")
    abstract suspend fun getStudent(id: Long): StudentEntityModel?

    @Transaction
    @Query("SELECT * FROM student WHERE id = :id")
    abstract fun getStudentFlow(id: Long): Flow<StudentEntityModel?>

    @Query("SELECT * FROM student WHERE group_id = :groupID")
    abstract fun getEntitiesFlow(groupID: Long): Flow<List<StudentEntity>>
}
