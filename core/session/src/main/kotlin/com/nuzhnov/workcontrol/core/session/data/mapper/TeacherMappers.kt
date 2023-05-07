package com.nuzhnov.workcontrol.core.session.data.mapper

import com.nuzhnov.workcontrol.core.model.Teacher
import com.nuzhnov.workcontrol.core.api.dto.university.TeacherDTO
import com.nuzhnov.workcontrol.core.database.entity.TeacherEntity


internal fun TeacherDTO.toTeacher(): Teacher = Teacher(
    id = id,
    name = name,
    email = email
)

internal fun TeacherEntity.toTeacher(): Teacher = Teacher(
    id = id,
    name = name,
    email = email
)

internal fun Teacher.toTeacherEntity(): TeacherEntity = TeacherEntity(
    id = id,
    name = name,
    email = email
)
