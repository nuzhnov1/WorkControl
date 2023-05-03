package com.nuzhnov.workcontrol.core.api.service

import com.nuzhnov.workcontrol.core.api.dto.lesson.*
import retrofit2.http.GET
import retrofit2.http.Query

interface LessonService {
    @GET("/lessons")
    suspend fun getTeacherLessons(@Query("teacher_id") teacherID: Long): List<LessonDTO>

    @GET("/lessons")
    suspend fun getTeacherDisciplineLessons(
        @Query("teacher_id") teacherID: Long,
        @Query("discipline_id") disciplineID: Long
    ): List<LessonDTO>

    @GET("/participants")
    suspend fun getParticipants(@Query("lesson_id") lessonID: Long): List<ParticipantModelDTO>

    @GET("/participants")
    suspend fun getStudentParticipationOfLessons(
        @Query("student_id") studentID: Long
    ): List<ParticipantLessonModelDTO>

    @GET("/participants")
    suspend fun getStudentParticipationOfTeacherLessons(
        @Query("student_id") studentID: Long,
        @Query("teacher_id") teacherID: Long
    ): List<ParticipantLessonModelDTO>
}
