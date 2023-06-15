package com.nuzhnov.workcontrol.core.works.data.work.clear

import com.nuzhnov.workcontrol.core.data.database.dao.LessonDAO
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class ClearLessonsWork @Inject constructor(
    private val lessonDAO: LessonDAO
) {

    suspend operator fun invoke() = safeExecute {
        lessonDAO.getFinishedEntitiesWithParticipants()
            .filter { complexEntity ->
                val isLessonSync = complexEntity.lessonEntity.isSynchronised
                val isAllParticipantsSync = complexEntity
                    .participantEntityList
                    .all { entity -> entity.isSynchronized }

                isLessonSync && isAllParticipantsSync
            }
            .map { complexEntity -> complexEntity.lessonEntity }
            .toTypedArray()
            .let { lessonEntities -> lessonDAO.delete(*lessonEntities) }
    }
}
