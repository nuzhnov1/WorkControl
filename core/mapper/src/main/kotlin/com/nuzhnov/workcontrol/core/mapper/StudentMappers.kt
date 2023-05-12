package com.nuzhnov.workcontrol.core.mapper

import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.model.Group
import com.nuzhnov.workcontrol.core.api.dto.university.StudentDTO
import com.nuzhnov.workcontrol.core.api.dto.user.StudentModelDTO
import com.nuzhnov.workcontrol.core.database.entity.StudentEntity
import com.nuzhnov.workcontrol.core.database.entity.model.StudentModel


fun StudentDTO.toStudent(group: Group): Student = Student(
    id = id,
    name = name,
    email = email,
    group = group
)

fun StudentDTO.toStudentEntity(groupID: Long): StudentEntity = StudentEntity(
    id = id,
    name = name,
    email = email,
    groupID = groupID
)

fun StudentModelDTO.toStudent(): Student = Student(
    id = studentDTO.id,
    name = studentDTO.name,
    email = studentDTO.email,
    group = groupModelDTO.toGroup()
)

fun StudentModelDTO.toStudentEntity(): StudentEntity = StudentEntity(
    id = studentDTO.id,
    name = studentDTO.name,
    email = studentDTO.email,
    groupID = groupModelDTO.groupDTO.id
)

fun StudentEntity.toStudent(group: Group): Student = Student(
    id = id,
    name = name,
    email = email,
    group = group
)

fun StudentModel.toStudent(): Student = Student(
    id = studentEntity.id,
    name = studentEntity.name,
    email = studentEntity.email,
    group = groupModel.toGroup()
)

fun Student.toStudentEntity(): StudentEntity = StudentEntity(
    id = id,
    name = name,
    email = email,
    groupID = group.id
)
