package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.TeacherDisciplineCrossRefEntity
import androidx.room.Dao

@Dao
abstract class TeacherDisciplineCrossRefDAO :
    EntityDAO<TeacherDisciplineCrossRefEntity>(entityName = "teacher_discipline_cross_ref")
