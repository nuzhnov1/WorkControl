package com.nuzhnov.workcontrol.core.api.service

import com.nuzhnov.workcontrol.core.api.dto.lesson.*
import com.nuzhnov.workcontrol.core.api.annotation.PermittedTo
import com.nuzhnov.workcontrol.core.model.Role
import retrofit2.http.GET
import retrofit2.http.Query

interface LessonService {
    // Примечание: извлекаются занятия текущего преподавателя по его токену
    @[GET("/lessons") PermittedTo(Role.TEACHER)]
    suspend fun getFinishedLessons(): List<LessonDTO>

    // Примечание: извлекаются занятия текущего преподавателя по его токену и по заданной дисциплине
    @[GET("/lessons") PermittedTo(Role.TEACHER)]
    suspend fun getDisciplineFinishedLessons(
        @Query("discipline_id") disciplineID: Long
    ): List<LessonDTO>

    // Примечание: преподавателю разрешено извлекать участников только своих занятий.
    // На сервере должна осуществляться проверка принадлежности запрашиваемого занятия к
    // данному преподавателю. Если занятие не принадлежит преподавателю - выдать ошибку
    @[GET("/participants") PermittedTo(Role.TEACHER)]
    suspend fun getParticipantsOfFinishedTeacherLesson(
        @Query("lesson_id") lessonID: Long
    ): List<ParticipantModelDTO>

    // Примечание: информация о посещениях указанного студента извлекается только относительно
    // занятий данного преподавателя по его токену
    @[GET("/participants") PermittedTo(Role.TEACHER)]
    suspend fun getStudentParticipationOfFinishedTeacherLessons(
        @Query("student_id") studentID: Long
    ): List<ParticipantLessonModelDTO>

    // Примечание: id студента извлекается на основе токена в заголовке пакета
    @[GET("/participants") PermittedTo(Role.STUDENT)]
    suspend fun getStudentParticipationOfFinishedLessons(): List<ParticipantLessonModelDTO>
}
