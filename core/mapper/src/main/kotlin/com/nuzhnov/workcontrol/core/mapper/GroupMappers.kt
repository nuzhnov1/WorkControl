package com.nuzhnov.workcontrol.core.mapper

import com.nuzhnov.workcontrol.core.model.Group
import com.nuzhnov.workcontrol.core.model.Faculty
import com.nuzhnov.workcontrol.core.api.dto.university.GroupDTO
import com.nuzhnov.workcontrol.core.api.dto.university.GroupModelDTO
import com.nuzhnov.workcontrol.core.database.entity.GroupEntity
import com.nuzhnov.workcontrol.core.database.entity.model.GroupModel


fun GroupDTO.toGroup(faculty: Faculty): Group = Group(
    id = id,
    name = name,
    course = course,
    faculty = faculty
)

fun GroupDTO.toGroupEntity(facultyID: Long): GroupEntity = GroupEntity(
    id = id,
    name = name,
    course = course,
    facultyID = facultyID
)

fun GroupModelDTO.toGroup(): Group = Group(
    id = groupDTO.id,
    name = groupDTO.name,
    course = groupDTO.course,
    faculty = facultyDTO.toFaculty()
)

fun GroupModelDTO.toGroupModel(): GroupModel = GroupModel(
    groupEntity = this.toGroupEntity(),
    facultyEntity = facultyDTO.toFacultyEntity()
)

fun GroupModelDTO.toGroupEntity(): GroupEntity = GroupEntity(
    id = groupDTO.id,
    name = groupDTO.name,
    course = groupDTO.course,
    facultyID = facultyDTO.id
)

fun GroupEntity.toGroup(faculty: Faculty): Group = Group(
    id = id,
    name = name,
    course = course,
    faculty = faculty
)

fun GroupModel.toGroup(): Group = Group(
    id = groupEntity.id,
    name = groupEntity.name,
    course = groupEntity.course,
    faculty = facultyEntity.toFaculty()
)

fun Group.toGroupEntity(): GroupEntity = GroupEntity(
    id = id,
    name = name,
    course = course,
    facultyID = faculty.id
)

fun Group.toGroupModel(): GroupModel = GroupModel(
    groupEntity = this.toGroupEntity(),
    facultyEntity = faculty.toFacultyEntity()
)
