package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.model.Faculty
import com.nuzhnov.workcontrol.core.data.api.dto.university.FacultyDTO
import com.nuzhnov.workcontrol.core.data.database.entity.FacultyEntity


fun FacultyDTO.toFaculty(): Faculty = Faculty(id = id, name = name)

fun FacultyDTO.toFacultyEntity(): FacultyEntity = FacultyEntity(id = id, name = name)

fun FacultyEntity.toFaculty(): Faculty = Faculty(id = id, name = name)

fun Faculty.toFacultyEntity(): FacultyEntity = FacultyEntity(id = id, name = name)
