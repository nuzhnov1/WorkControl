package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.model.Teacher
import com.nuzhnov.workcontrol.core.data.api.dto.university.TeacherDTO
import com.nuzhnov.workcontrol.core.data.api.dto.user.TeacherModelDTO
import com.nuzhnov.workcontrol.core.data.database.entity.TeacherEntity


fun TeacherDTO.toTeacher(): Teacher = Teacher(
    id = id,
    name = name,
    email = email
)

fun TeacherDTO.toTeacherEntity(): TeacherEntity = TeacherEntity(
    id = id,
    name = name,
    email = email
)

fun TeacherModelDTO.toTeacherEntity(): TeacherEntity = TeacherEntity(
    id = teacherDTO.id,
    name = teacherDTO.name,
    email = teacherDTO.email
)

fun TeacherEntity.toTeacher(): Teacher = Teacher(
    id = id,
    name = name,
    email = email
)

fun Teacher.toTeacherEntity(): TeacherEntity = TeacherEntity(
    id = id,
    name = name,
    email = email
)
