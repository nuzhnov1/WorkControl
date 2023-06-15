package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.models.Building
import com.nuzhnov.workcontrol.core.data.api.dto.university.BuildingDTO
import com.nuzhnov.workcontrol.core.data.database.entity.BuildingEntity


fun BuildingDTO.toBuildingEntity() = BuildingEntity(id = id, name = name)

fun BuildingEntity.toBuilding() = Building(id = id, name = name)

fun Building.toBuildingEntity() = BuildingEntity(id = id, name = name)
