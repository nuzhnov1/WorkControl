package com.nuzhnov.workcontrol.core.session.data.mapper

import com.nuzhnov.workcontrol.core.model.Group
import com.nuzhnov.workcontrol.core.api.dto.university.GroupModelDTO
import com.nuzhnov.workcontrol.core.database.entity.GroupEntity
import com.nuzhnov.workcontrol.core.database.entity.model.GroupModel


internal fun GroupModelDTO.toGroup(): Group = Group(
    id = groupDTO.id,
    name = groupDTO.name,
    course = groupDTO.course,
    faculty = facultyDTO.toFaculty()
)

internal fun GroupModel.toGroup(): Group = Group(
    id = groupEntity.id,
    name = groupEntity.name,
    course = groupEntity.course,
    faculty = facultyEntity.toFaculty()
)

internal fun Group.toGroupEntity(): GroupEntity = GroupEntity(
    id = id,
    name = name,
    course = course,
    facultyID = faculty.id
)
