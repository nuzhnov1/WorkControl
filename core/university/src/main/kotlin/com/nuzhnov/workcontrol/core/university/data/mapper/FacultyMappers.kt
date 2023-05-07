package com.nuzhnov.workcontrol.core.university.data.mapper

import com.nuzhnov.workcontrol.core.model.Faculty
import com.nuzhnov.workcontrol.core.api.dto.university.FacultyDTO
import com.nuzhnov.workcontrol.core.database.entity.FacultyEntity


internal fun FacultyDTO.toFaculty(): Faculty = Faculty(id = id, name = name)

internal fun FacultyEntity.toFaculty(): Faculty = Faculty(id = id, name = name)

internal fun Faculty.toFacultyEntity(): FacultyEntity = FacultyEntity(id = id, name = name)
