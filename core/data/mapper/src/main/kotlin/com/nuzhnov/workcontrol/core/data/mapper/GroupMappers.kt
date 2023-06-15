package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.models.Group
import com.nuzhnov.workcontrol.core.models.Department
import com.nuzhnov.workcontrol.core.data.api.dto.university.GroupDTO
import com.nuzhnov.workcontrol.core.data.api.dto.university.GroupDTOModel
import com.nuzhnov.workcontrol.core.data.database.entity.GroupEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.GroupEntityModel


fun GroupDTO.toGroupEntity(departmentID: Long) = GroupEntity(
    id = id,
    name = name,
    course = course,
    departmentID = departmentID
)

fun GroupDTOModel.toGroup() = Group(
    id = groupDTO.id,
    name = groupDTO.name,
    course = groupDTO.course,
    department = departmentDTO.toDepartment()
)

fun GroupDTOModel.toGroupEntityModel() = GroupEntityModel(
    groupEntity = this.toGroupEntity(),
    departmentEntity = departmentDTO.toDepartmentEntity()
)

fun GroupDTOModel.toGroupEntity() = GroupEntity(
    id = groupDTO.id,
    name = groupDTO.name,
    course = groupDTO.course,
    departmentID = departmentDTO.id
)

fun GroupEntity.toGroup(department: Department) = Group(
    id = id,
    name = name,
    course = course,
    department = department
)

fun GroupEntityModel.toGroup() = Group(
    id = groupEntity.id,
    name = groupEntity.name,
    course = groupEntity.course,
    department = departmentEntity.toDepartment()
)

fun Group.toGroupEntity() = GroupEntity(
    id = id,
    name = name,
    course = course,
    departmentID = department.id
)

fun Group.toGroupEntityModel() = GroupEntityModel(
    groupEntity = this.toGroupEntity(),
    departmentEntity = department.toDepartmentEntity()
)
