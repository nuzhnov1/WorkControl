package com.nuzhnov.workcontrol.core.mapper

import com.nuzhnov.workcontrol.core.model.Discipline
import com.nuzhnov.workcontrol.core.api.dto.university.DisciplineDTO
import com.nuzhnov.workcontrol.core.database.entity.DisciplineEntity


fun DisciplineDTO.toDiscipline(): Discipline = Discipline(id = id, name = name)

fun DisciplineDTO.toDisciplineEntity(): DisciplineEntity = DisciplineEntity(id = id, name = name)

fun DisciplineEntity.toDiscipline(): Discipline = Discipline(id = id, name = name)

fun Discipline.toDisciplineEntity(): DisciplineEntity = DisciplineEntity(id = id, name = name)
