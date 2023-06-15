package com.nuzhnov.workcontrol.core.works.data.work.sync

import com.nuzhnov.workcontrol.core.data.api.service.LessonService
import com.nuzhnov.workcontrol.core.data.database.dao.LessonDAO
import com.nuzhnov.workcontrol.core.data.database.dao.ParticipantDAO
import com.nuzhnov.workcontrol.core.data.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.data.mapper.toNewLessonDTO
import com.nuzhnov.workcontrol.core.data.mapper.toUpdatedParticipantDTO
import com.nuzhnov.workcontrol.core.models.Role
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class SyncLessonsWork @Inject constructor(
    private val lessonService: LessonService,
    private val lessonDAO: LessonDAO,
    private val participantDAO: ParticipantDAO,
    private val appPreferences: AppPreferences
) {

    suspend operator fun invoke() = safeExecute {
        val session = appPreferences.getSession()

        if (session?.role == Role.TEACHER) {
            syncParticipants()
            syncLessons()
        }
    }

    private suspend fun syncParticipants() = lessonDAO
        .getFinishedLessonEntities()
        .filter { entity -> entity.isSynchronised }
        .map { entity -> entity.id }
        .let { idList -> participantDAO.getEntities(idList) }
        .filterNot { entity -> entity.isSynchronized }
        .let { entityList ->
            if (entityList.isEmpty()) {
                return@let
            }

            entityList
                .map(ParticipantEntity::toUpdatedParticipantDTO)
                .let { dtoList -> lessonService.postUpdatedParticipants(dtoList) }

            entityList
                .map { entity -> entity.copy(isSynchronized = true) }
                .toTypedArray()
                .let { entities -> participantDAO.insertOrUpdate(*entities) }
        }

    private suspend fun syncLessons() = lessonDAO
        .getFinishedLessonEntities()
        .filterNot { entity -> entity.isSynchronised }
        .let { entityList ->
            if (entityList.isEmpty()) {
                return@let
            }

            val idList = entityList.map { entity -> entity.id }
            val participantEntityList = participantDAO.getEntities(idList)

            entityList
                .map { lessonEntity ->
                    participantEntityList
                        .filter { entity -> entity.lessonID == lessonEntity.id }
                        .let { associatedParticipants -> lessonEntity to associatedParticipants }
                        .toNewLessonDTO()
                }
                .let { dtoList -> lessonService.postNewLessons(dtoList) }

            entityList
                .map { entity -> entity.copy(isSynchronised = true) }
                .toTypedArray()
                .let { entities -> lessonDAO.insertOrUpdate(*entities) }

            participantEntityList
                .map { entity -> entity.copy(isSynchronized = true) }
                .toTypedArray()
                .let { entities -> participantDAO.insertOrUpdate(*entities) }
        }
}
