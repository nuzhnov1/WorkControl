package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.models.Student
import com.nuzhnov.workcontrol.core.models.Group
import com.nuzhnov.workcontrol.core.data.api.dto.university.StudentDTO
import com.nuzhnov.workcontrol.core.data.api.dto.user.StudentModelDTO
import com.nuzhnov.workcontrol.core.data.database.entity.StudentEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.StudentModel


fun StudentDTO.toStudentEntity(groupID: Long) = StudentEntity(
    id = id,
    name = name,
    email = email,
    groupID = groupID
)

fun StudentModelDTO.toStudent() = Student(
    id = studentDTO.id,
    name = studentDTO.name,
    email = studentDTO.email,
    group = groupModelDTO.toGroup()
)

fun StudentModelDTO.toStudentEntity() = StudentEntity(
    id = studentDTO.id,
    name = studentDTO.name,
    email = studentDTO.email,
    groupID = groupModelDTO.groupDTO.id
)

fun StudentEntity.toStudent(group: Group) = Student(
    id = id,
    name = name,
    email = email,
    group = group
)

fun StudentModel.toStudent() = Student(
    id = studentEntity.id,
    name = studentEntity.name,
    email = studentEntity.email,
    group = groupModel.toGroup()
)

fun Student.toStudentEntity() = StudentEntity(
    id = id,
    name = name,
    email = email,
    groupID = group.id
)
