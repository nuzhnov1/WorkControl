package com.nuzhnov.workcontrol.core.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Example response:
 * {
 *      room: {
 *          id: 1,
 *          name: "7-220"
 *      },
 *
 *      building: {
 *          id: 1,
 *          name: "Seven building"
 *      }
 * }
 */
@JsonClass(generateAdapter = true)
data class RoomWithBuildingDTO(
    @Json(name = "room") val roomDTO: RoomDTO,
    @Json(name = "building") val buildingDTO: BuildingDTO
)
