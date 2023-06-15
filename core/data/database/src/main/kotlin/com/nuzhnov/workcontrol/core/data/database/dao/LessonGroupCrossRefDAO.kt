package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.LessonGroupCrossRefEntity
import androidx.room.Dao

@Dao
abstract class LessonGroupCrossRefDAO :
    EntityDAO<LessonGroupCrossRefEntity>(entityName = "lesson_student_group_cross_ref")
