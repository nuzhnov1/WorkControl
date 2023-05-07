package com.nuzhnov.workcontrol.core.work.data.mapper

import com.nuzhnov.workcontrol.core.api.dto.university.BuildingDTO
import com.nuzhnov.workcontrol.core.database.entity.BuildingEntity


internal fun BuildingDTO.toBuildingEntity(): BuildingEntity = BuildingEntity(id = id, name = name)
