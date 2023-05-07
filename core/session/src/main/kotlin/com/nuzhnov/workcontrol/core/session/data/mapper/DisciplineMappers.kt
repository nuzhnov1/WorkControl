package com.nuzhnov.workcontrol.core.session.data.mapper

import com.nuzhnov.workcontrol.core.model.Discipline
import com.nuzhnov.workcontrol.core.api.dto.university.DisciplineDTO
import com.nuzhnov.workcontrol.core.database.entity.DisciplineEntity


internal fun DisciplineDTO.toDiscipline(): Discipline = Discipline(
    id = id,
    name = name
)

internal fun DisciplineEntity.toDiscipline(): Discipline = Discipline(
    id = id,
    name = name
)

internal fun Discipline.toDisciplineEntity(): DisciplineEntity = DisciplineEntity(
    id = id,
    name = name
)
