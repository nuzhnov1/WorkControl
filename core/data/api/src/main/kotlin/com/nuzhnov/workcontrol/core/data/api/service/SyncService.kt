package com.nuzhnov.workcontrol.core.data.api.service

import com.nuzhnov.workcontrol.core.data.api.dto.university.*
import com.nuzhnov.workcontrol.core.data.api.dto.lesson.*
import com.nuzhnov.workcontrol.core.data.api.dto.user.StudentModelDTO
import com.nuzhnov.workcontrol.core.data.api.annotation.PermittedTo
import com.nuzhnov.workcontrol.core.models.Role
import retrofit2.http.POST
import retrofit2.http.Body

interface SyncService {
    @[POST("/buildings?list") PermittedTo(Role.TEACHER)]
    suspend fun getBuildings(@Body buildingIDList: List<Long>): List<BuildingDTO>

    @[POST("/rooms?list") PermittedTo(Role.TEACHER)]
    suspend fun getRooms(@Body roomIDList: List<Long>): List<RoomModelDTO>

    @[POST("/departments?list") PermittedTo(Role.TEACHER)]
    suspend fun getDepartments(@Body departmentIDList: List<Long>): List<DepartmentDTO>

    @[POST("/groups?list") PermittedTo(Role.TEACHER)]
    suspend fun getGroups(@Body groupIDList: List<Long>): List<GroupModelDTO>

    @[POST("/students?list") PermittedTo(Role.TEACHER)]
    suspend fun getStudents(@Body studentIDList: List<Long>): List<StudentModelDTO>

    // Примечание: на сервере должна осуществляться проверка на то, что занятие было создано
    // данным преподавателем с его дисциплиной
    @[POST("/lessons?new") PermittedTo(Role.TEACHER)]
    suspend fun postNewLessons(@Body newLessonDTOList: List<NewLessonDTO>)

    // Примечание: на сервере должна осуществляться проверка на то, что участники могут обновляться
    // только для занятий данного преподавателя
    @[POST("/participants?update") PermittedTo(Role.TEACHER)]
    suspend fun postUpdatedParticipants(@Body updatedParticipantDTOList: List<UpdatedParticipantDTO>)
}
