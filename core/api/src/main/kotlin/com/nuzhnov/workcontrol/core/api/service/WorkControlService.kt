package com.nuzhnov.workcontrol.core.api.service

import com.nuzhnov.workcontrol.core.api.model.*

interface WorkControlService {
    suspend fun getBuildings(): List<BuildingDTO>

    suspend fun getRooms(buildingID: Long): List<RoomDTO>

    suspend fun getFaculties(): List<FacultyDTO>

    suspend fun getStudentGroups(facultyID: Long): List<StudentGroupDTO>

    suspend fun getStudents(studentGroupID: Long): List<StudentDTO>

    suspend fun getTeacherDisciplines(teacherID: Long): List<DisciplineDTO>

    suspend fun getTeacherLessons(teacherID: Long): List<LessonWithoutTeacherDTO>

    suspend fun getTeacherDisciplineLessons(
        teacherID: Long,
        disciplineID: Long
    ): List<LessonWithoutTeacherAndDisciplineDTO>

    suspend fun postLesson(lessonPostedDTO: LessonPostedDTO)

    suspend fun getStudentParticipants(studentID: Long): List<ParticipantWithoutStudentDTO>

    suspend fun getLessonParticipants(lessonID: Long): List<ParticipantWithoutLessonDTO>
}
