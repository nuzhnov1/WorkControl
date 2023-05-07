package com.nuzhnov.workcontrol.core.work.data.mapper

import com.nuzhnov.workcontrol.core.api.dto.user.StudentModelDTO
import com.nuzhnov.workcontrol.core.database.entity.StudentEntity


internal fun StudentModelDTO.toStudentEntity(): StudentEntity = StudentEntity(
    id = studentDTO.id,
    name = studentDTO.name,
    email = studentDTO.email,
    groupID = groupModelDTO.groupDTO.id
)
