package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.model.Group
import com.nuzhnov.workcontrol.core.model.Department
import com.nuzhnov.workcontrol.core.data.api.dto.university.GroupDTO
import com.nuzhnov.workcontrol.core.data.api.dto.university.GroupModelDTO
import com.nuzhnov.workcontrol.core.data.database.entity.GroupEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.GroupModel


fun GroupDTO.toGroup(department: Department): Group = Group(
    id = id,
    name = name,
    course = course,
    department = department
)

fun GroupDTO.toGroupEntity(departmentID: Long): GroupEntity = GroupEntity(
    id = id,
    name = name,
    course = course,
    departmentID = departmentID
)

fun GroupModelDTO.toGroup(): Group = Group(
    id = groupDTO.id,
    name = groupDTO.name,
    course = groupDTO.course,
    department = departmentDTO.toDepartment()
)

fun GroupModelDTO.toGroupModel(): GroupModel = GroupModel(
    groupEntity = this.toGroupEntity(),
    departmentEntity = departmentDTO.toDepartmentEntity()
)

fun GroupModelDTO.toGroupEntity(): GroupEntity = GroupEntity(
    id = groupDTO.id,
    name = groupDTO.name,
    course = groupDTO.course,
    departmentID = departmentDTO.id
)

fun GroupEntity.toGroup(department: Department): Group = Group(
    id = id,
    name = name,
    course = course,
    department = department
)

fun GroupModel.toGroup(): Group = Group(
    id = groupEntity.id,
    name = groupEntity.name,
    course = groupEntity.course,
    department = departmentEntity.toDepartment()
)

fun Group.toGroupEntity(): GroupEntity = GroupEntity(
    id = id,
    name = name,
    course = course,
    departmentID = department.id
)

fun Group.toGroupModel(): GroupModel = GroupModel(
    groupEntity = this.toGroupEntity(),
    departmentEntity = department.toDepartmentEntity()
)
