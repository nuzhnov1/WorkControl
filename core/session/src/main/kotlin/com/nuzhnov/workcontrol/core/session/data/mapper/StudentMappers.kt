package com.nuzhnov.workcontrol.core.session.data.mapper

import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.api.dto.user.StudentModelDTO
import com.nuzhnov.workcontrol.core.database.entity.StudentEntity
import com.nuzhnov.workcontrol.core.database.entity.model.StudentModel


internal fun StudentModelDTO.toStudent(): Student = Student(
    id = studentDTO.id,
    name = studentDTO.name,
    email = studentDTO.email,
    group = groupModelDTO.toGroup()
)

internal fun StudentModel.toStudent(): Student = Student(
    id = studentEntity.id,
    name = studentEntity.name,
    email = studentEntity.email,
    group = groupModel.toGroup()
)

internal fun Student.toStudentEntity(): StudentEntity = StudentEntity(
    id = id,
    name = name,
    email = email,
    groupID = group.id
)
