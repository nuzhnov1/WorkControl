package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.model.Building
import com.nuzhnov.workcontrol.core.data.api.dto.university.BuildingDTO
import com.nuzhnov.workcontrol.core.data.database.entity.BuildingEntity


fun BuildingDTO.toBuilding(): Building = Building(id = id, name = name)

fun BuildingDTO.toBuildingEntity(): BuildingEntity = BuildingEntity(id = id, name = name)

fun BuildingEntity.toBuilding(): Building = Building(id = id, name = name)

fun Building.toBuildingEntity(): BuildingEntity = BuildingEntity(id = id, name = name)
