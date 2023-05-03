package com.nuzhnov.workcontrol.core.api.service

import com.nuzhnov.workcontrol.core.api.dto.university.*
import com.nuzhnov.workcontrol.core.api.dto.lesson.*
import com.nuzhnov.workcontrol.core.api.dto.user.TeacherModelDTO
import com.nuzhnov.workcontrol.core.api.dto.user.StudentModelDTO
import retrofit2.http.POST
import retrofit2.http.Body

interface SyncService {
    @POST("/buildings?list")
    suspend fun getBuildings(@Body buildingIDList: List<Long>): List<BuildingDTO>

    @POST("/rooms?list")
    suspend fun getRooms(@Body roomIDList: List<Long>): List<RoomModelDTO>

    @POST("/disciplines?list")
    suspend fun getDisciplines(@Body disciplineIDList: List<Long>): List<DisciplineDTO>

    @POST("/faculties?list")
    suspend fun getFaculties(@Body facultyIDList: List<Long>): List<FacultyDTO>

    @POST("/groups?list")
    suspend fun getGroups(@Body groupIDList: List<Long>): List<GroupModelDTO>

    @POST("/students?list")
    suspend fun getStudents(@Body studentIDList: List<Long>): List<StudentModelDTO>

    @POST("/teachers?list")
    suspend fun getTeachers(@Body teacherIDList: List<Long>): List<TeacherModelDTO>

    @POST("/lessons?list")
    suspend fun getLessons(@Body lessonIDList: List<Long>): List<LessonDTO>

    @POST("/lessons?new")
    suspend fun postNewLessons(@Body newLessonDTOList: List<NewLessonDTO>)

    @POST("/participants?update")
    suspend fun postUpdatedParticipants(@Body updatedParticipantDTOList: List<UpdatedParticipantDTO>)
}
