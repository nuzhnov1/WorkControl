package com.nuzhnov.workcontrol.core.university.data.mapper

import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.model.Group
import com.nuzhnov.workcontrol.core.api.dto.university.StudentDTO
import com.nuzhnov.workcontrol.core.database.entity.StudentEntity


internal fun StudentDTO.toStudent(group: Group): Student = Student(
    id = id,
    name = name,
    email = email,
    group = group
)

internal fun StudentEntity.toStudent(group: Group): Student = Student(
    id = id,
    name = name,
    email = email,
    group = group
)

internal fun Student.toStudentEntity(): StudentEntity = StudentEntity(
    id = id,
    name = name,
    email = email,
    groupID = group.id
)
