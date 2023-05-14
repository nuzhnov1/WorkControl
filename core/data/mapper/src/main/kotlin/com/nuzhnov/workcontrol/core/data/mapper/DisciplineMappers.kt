package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.model.Discipline
import com.nuzhnov.workcontrol.core.data.api.dto.university.DisciplineDTO
import com.nuzhnov.workcontrol.core.data.database.entity.DisciplineEntity


fun DisciplineDTO.toDiscipline(): Discipline = Discipline(id = id, name = name)

fun DisciplineDTO.toDisciplineEntity(): DisciplineEntity = DisciplineEntity(id = id, name = name)

fun DisciplineEntity.toDiscipline(): Discipline = Discipline(id = id, name = name)

fun Discipline.toDisciplineEntity(): DisciplineEntity = DisciplineEntity(id = id, name = name)
