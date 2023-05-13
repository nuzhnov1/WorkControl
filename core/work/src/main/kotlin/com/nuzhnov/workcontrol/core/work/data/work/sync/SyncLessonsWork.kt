package com.nuzhnov.workcontrol.core.work.data.work.sync

import com.nuzhnov.workcontrol.core.data.api.service.SyncService
import com.nuzhnov.workcontrol.core.data.database.dao.LessonDAO
import com.nuzhnov.workcontrol.core.data.database.dao.ParticipantDAO
import com.nuzhnov.workcontrol.core.data.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.data.mapper.toNewLessonDTO
import com.nuzhnov.workcontrol.core.data.mapper.toUpdatedParticipantDTO
import com.nuzhnov.workcontrol.core.model.Role
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class SyncLessonsWork @Inject constructor(
    private val syncService: SyncService,
    private val lessonDAO: LessonDAO,
    private val participantDAO: ParticipantDAO,
    private val appPreferences: AppPreferences
) {

    suspend operator fun invoke(): Result<Unit> = safeExecute {
        val session = appPreferences.getSession()

        if (session?.role == Role.TEACHER) {
            syncParticipants()
            syncLessons()
        }
    }

    private suspend fun syncParticipants(): Unit = lessonDAO
        .getFinishedLessonEntities()
        .filter { lessonEntity -> lessonEntity.isSynchronised }
        .map { lessonEntity -> lessonEntity.id }
        .let { lessonEntityIDList -> participantDAO.getEntities(lessonEntityIDList) }
        .filterNot { participantEntity -> participantEntity.isSynchronized }
        .let { participantEntityList ->
            if (participantEntityList.isEmpty()) {
                return@let
            }

            participantEntityList
                .map(ParticipantEntity::toUpdatedParticipantDTO)
                .let { dtoList -> syncService.postUpdatedParticipants(dtoList) }

            participantEntityList
                .map { participantEntity -> participantEntity.copy(isSynchronized = true) }
                .toTypedArray()
                .let { entities -> participantDAO.insertOrUpdate(*entities) }
        }

    private suspend fun syncLessons(): Unit = lessonDAO
        .getFinishedLessonEntities()
        .filterNot { lessonEntity -> lessonEntity.isSynchronised }
        .let { lessonEntityList ->
            if (lessonEntityList.isEmpty()) {
                return@let
            }

            val lessonEntityIDList = lessonEntityList.map { lessonEntity -> lessonEntity.id }
            val participantEntityList = participantDAO.getEntities(lessonEntityIDList)

            lessonEntityList
                .map { lessonEntity ->
                    participantEntityList
                        .filter { entity -> entity.lessonID == lessonEntity.id }
                        .let { associatedParticipants -> lessonEntity to associatedParticipants }
                        .toNewLessonDTO()
                }
                .let { dtoList -> syncService.postNewLessons(newLessonDTOList = dtoList) }

            lessonEntityList
                .map { lessonEntity -> lessonEntity.copy(isSynchronised = true) }
                .toTypedArray()
                .let { entities -> lessonDAO.insertOrUpdate(*entities) }

            participantEntityList
                .map { participantEntity -> participantEntity.copy(isSynchronized = true) }
                .toTypedArray()
                .let { entities -> participantDAO.insertOrUpdate(*entities) }
        }
}
