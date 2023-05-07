package com.nuzhnov.workcontrol.core.work.data.mapper

import com.nuzhnov.workcontrol.core.api.dto.university.GroupModelDTO
import com.nuzhnov.workcontrol.core.database.entity.GroupEntity


internal fun GroupModelDTO.toGroupEntity(): GroupEntity = GroupEntity(
    id = groupDTO.id,
    name = groupDTO.name,
    course = groupDTO.course,
    facultyID = facultyDTO.id
)
