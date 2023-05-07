package com.nuzhnov.workcontrol.core.work.data.mapper

import com.nuzhnov.workcontrol.core.api.dto.university.FacultyDTO
import com.nuzhnov.workcontrol.core.database.entity.FacultyEntity


internal fun FacultyDTO.toFacultyEntity(): FacultyEntity = FacultyEntity(id = id, name = name)
