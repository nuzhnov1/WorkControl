package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.model.Department
import com.nuzhnov.workcontrol.core.data.api.dto.university.DepartmentDTO
import com.nuzhnov.workcontrol.core.data.database.entity.DepartmentEntity


fun DepartmentDTO.toDepartment(): Department = Department(id = id, name = name)

fun DepartmentDTO.toDepartmentEntity(): DepartmentEntity = DepartmentEntity(id = id, name = name)

fun DepartmentEntity.toDepartment(): Department = Department(id = id, name = name)

fun Department.toDepartmentEntity(): DepartmentEntity = DepartmentEntity(id = id, name = name)
