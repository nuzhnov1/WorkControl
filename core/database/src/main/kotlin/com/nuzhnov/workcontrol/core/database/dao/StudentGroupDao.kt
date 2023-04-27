package com.nuzhnov.workcontrol.core.database.dao

import com.nuzhnov.workcontrol.core.database.entity.StudentGroupEntity
import com.nuzhnov.workcontrol.core.database.models.StudentGroupWithFaculty
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface StudentGroupDao : BaseDao<StudentGroupEntity> {
    @Query(FETCH_QUERY)
    fun getEntitiesFlow(): Flow<List<StudentGroupEntity>>

    @Query(FETCH_BY_FACULTY_ID_QUERY)
    fun getFacultyGroupsFlow(facultyID: Long): Flow<List<StudentGroupEntity>>

    @Query(FETCH_QUERY)
    suspend fun getEntities(): List<StudentGroupEntity>

    @Query(FETCH_BY_FACULTY_ID_QUERY)
    suspend fun getFacultyGroups(facultyID: Long): List<StudentGroupEntity>

    @[Transaction Query(FETCH_BY_STUDENT_GROUP_ID_QUERY)]
    suspend fun getStudentGroupWithFaculty(studentGroupID: Long): StudentGroupWithFaculty

    suspend fun clear() = getEntities().forEach { entity ->
        runCatching { delete(entity) }.onFailure { cause ->
            if (cause !is SQLiteConstraintException) {
                throw cause
            }
        }
    }


    private companion object {
        const val FETCH_QUERY = "SELECT * FROM student_group"
        const val FETCH_BY_FACULTY_ID_QUERY = "SELECT * FROM student_group WHERE faculty_id = :facultyID"
        const val FETCH_BY_STUDENT_GROUP_ID_QUERY = "SELECT * FROM student_group WHERE id = :studentGroupID"
    }
}
