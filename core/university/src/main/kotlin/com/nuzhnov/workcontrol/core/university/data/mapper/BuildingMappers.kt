package com.nuzhnov.workcontrol.core.university.data.mapper

import com.nuzhnov.workcontrol.core.model.Building
import com.nuzhnov.workcontrol.core.api.dto.university.BuildingDTO
import com.nuzhnov.workcontrol.core.database.entity.BuildingEntity


internal fun BuildingDTO.toBuilding(): Building = Building(id = id, name = name)

internal fun BuildingEntity.toBuilding(): Building = Building(id = id, name = name)

internal fun Building.toBuildingEntity(): BuildingEntity = BuildingEntity(id = id, name = name)
