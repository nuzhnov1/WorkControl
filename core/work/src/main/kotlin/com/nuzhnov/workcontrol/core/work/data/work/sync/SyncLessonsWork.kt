package com.nuzhnov.workcontrol.core.work.data.work.sync

import com.nuzhnov.workcontrol.core.work.data.mapper.toNewLessonDTO
import com.nuzhnov.workcontrol.core.work.data.mapper.toUpdatedParticipantDTO
import com.nuzhnov.workcontrol.core.api.service.SyncService
import com.nuzhnov.workcontrol.core.database.dao.LessonDAO
import com.nuzhnov.workcontrol.core.database.dao.ParticipantDAO
import com.nuzhnov.workcontrol.core.model.Lesson.State
import javax.inject.Inject

internal class SyncLessonsWork @Inject constructor(
    private val syncService: SyncService,
    private val lessonDAO: LessonDAO,
    private val participantDAO: ParticipantDAO
) {

    suspend operator fun invoke(): Result<Unit> = runCatching {
        syncParticipants()
        syncLessons()
    }

    private suspend fun syncParticipants(): Unit = lessonDAO
        .getEntities(state = State.FINISHED)
        .filter { lessonEntity -> lessonEntity.isSynchronised }
        .map { lessonEntity -> lessonEntity.id }
        .let { lessonEntityIDList -> participantDAO.getEntities(lessonEntityIDList) }
        .filterNot { participantEntity -> participantEntity.isSynchronized }
        .run {
            if (this.isEmpty()) {
                return@run
            }

            val updatedParticipantDTOList = this
                .map { participantEntity -> participantEntity.toUpdatedParticipantDTO() }

            syncService.postUpdatedParticipants(updatedParticipantDTOList)

            val synchronizedParticipantList = this
                .map { participantEntity -> participantEntity.copy(isSynchronized = true) }
                .toTypedArray()

            participantDAO.insertOrUpdate(*synchronizedParticipantList)
        }

    private suspend fun syncLessons(): Unit = lessonDAO
        .getEntities(state = State.FINISHED)
        .filterNot { lessonEntity -> lessonEntity.isSynchronised }
        .run {
            if (this.isEmpty()) {
                return@run
            }

            val lessonEntityIDList = this.map { lessonEntity -> lessonEntity.id }
            val participantEntityList = participantDAO.getEntities(lessonEntityIDList)

            val newLessonDTOList = this.map { lessonEntity ->
                participantEntityList
                    .filter { participantEntity -> participantEntity.lessonID == lessonEntity.id }
                    .let { associatedParticipants -> lessonEntity to associatedParticipants }
                    .toNewLessonDTO()
            }

            syncService.postNewLessons(newLessonDTOList)

            val synchronizedLessonEntityList = this
                .map { lessonEntity -> lessonEntity.copy(isSynchronised = true) }
                .toTypedArray()

            val synchronizedParticipantList = participantEntityList
                .map { participantEntity -> participantEntity.copy(isSynchronized = true) }
                .toTypedArray()

            lessonDAO.insertOrUpdate(*synchronizedLessonEntityList)
            participantDAO.insertOrUpdate(*synchronizedParticipantList)
        }
}
