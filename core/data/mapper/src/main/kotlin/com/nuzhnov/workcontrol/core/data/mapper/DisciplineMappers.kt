package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.models.Discipline
import com.nuzhnov.workcontrol.core.data.api.dto.university.DisciplineDTO
import com.nuzhnov.workcontrol.core.data.database.entity.DisciplineEntity


fun DisciplineDTO.toDiscipline() = Discipline(id = id, name = name)

fun DisciplineDTO.toDisciplineEntity() = DisciplineEntity(id = id, name = name)

fun DisciplineEntity.toDiscipline() = Discipline(id = id, name = name)

fun Discipline.toDisciplineEntity() = DisciplineEntity(id = id, name = name)
