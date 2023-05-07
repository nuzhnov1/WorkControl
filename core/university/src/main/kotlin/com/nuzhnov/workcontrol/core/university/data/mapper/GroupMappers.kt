package com.nuzhnov.workcontrol.core.university.data.mapper

import com.nuzhnov.workcontrol.core.model.Group
import com.nuzhnov.workcontrol.core.model.Faculty
import com.nuzhnov.workcontrol.core.api.dto.university.GroupDTO
import com.nuzhnov.workcontrol.core.database.entity.GroupEntity


internal fun GroupDTO.toGroup(faculty: Faculty): Group = Group(
    id = id,
    name = name,
    course = course,
    faculty = faculty
)

internal fun GroupEntity.toGroup(faculty: Faculty): Group = Group(
    id = id,
    name = name,
    course = course,
    faculty = faculty
)

internal fun Group.toGroupEntity(): GroupEntity = GroupEntity(
    id = id,
    name = name,
    course = course,
    facultyID = faculty.id
)
