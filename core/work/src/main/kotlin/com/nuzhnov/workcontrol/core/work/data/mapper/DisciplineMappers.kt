package com.nuzhnov.workcontrol.core.work.data.mapper

import com.nuzhnov.workcontrol.core.api.dto.university.DisciplineDTO
import com.nuzhnov.workcontrol.core.database.entity.DisciplineEntity


internal fun DisciplineDTO.toDisciplineEntity(): DisciplineEntity = DisciplineEntity(
    id = id,
    name = name
)
