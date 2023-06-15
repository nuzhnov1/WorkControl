package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.models.Student
import com.nuzhnov.workcontrol.core.models.Group
import com.nuzhnov.workcontrol.core.data.api.dto.university.StudentDTO
import com.nuzhnov.workcontrol.core.data.api.dto.user.StudentDTOModel
import com.nuzhnov.workcontrol.core.data.database.entity.StudentEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.StudentEntityModel


fun StudentDTO.toStudentEntity(groupID: Long) = StudentEntity(
    id = id,
    name = name,
    email = email,
    groupID = groupID
)

fun StudentDTOModel.toStudent() = Student(
    id = studentDTO.id,
    name = studentDTO.name,
    email = studentDTO.email,
    group = groupDTOModel.toGroup()
)

fun StudentDTOModel.toStudentEntity() = StudentEntity(
    id = studentDTO.id,
    name = studentDTO.name,
    email = studentDTO.email,
    groupID = groupDTOModel.groupDTO.id
)

fun StudentEntity.toStudent(group: Group) = Student(
    id = id,
    name = name,
    email = email,
    group = group
)

fun StudentEntityModel.toStudent() = Student(
    id = studentEntity.id,
    name = studentEntity.name,
    email = studentEntity.email,
    group = groupEntityModel.toGroup()
)

fun Student.toStudentEntity() = StudentEntity(
    id = id,
    name = name,
    email = email,
    groupID = group.id
)
