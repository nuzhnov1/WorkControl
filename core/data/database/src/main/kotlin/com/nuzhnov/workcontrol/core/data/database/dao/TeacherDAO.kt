package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.TeacherEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.TeacherEntityModel
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class TeacherDAO : EntityDAO<TeacherEntity>(entityName = "teacher") {
    @Transaction
    @Query("SELECT * FROM teacher WHERE id = :id")
    abstract suspend fun getTeacher(id: Long): TeacherEntityModel?

    @Transaction
    @Query("SELECT * FROM teacher WHERE id = :id")
    abstract fun getTeacherFlow(id: Long): Flow<TeacherEntityModel?>
}
