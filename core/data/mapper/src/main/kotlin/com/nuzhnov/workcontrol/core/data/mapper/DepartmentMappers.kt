package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.models.Department
import com.nuzhnov.workcontrol.core.data.api.dto.university.DepartmentDTO
import com.nuzhnov.workcontrol.core.data.database.entity.DepartmentEntity


fun DepartmentDTO.toDepartment() = Department(id = id, name = name)

fun DepartmentDTO.toDepartmentEntity() = DepartmentEntity(id = id, name = name)

fun DepartmentEntity.toDepartment() = Department(id = id, name = name)

fun Department.toDepartmentEntity() = DepartmentEntity(id = id, name = name)
