package com.nuzhnov.workcontrol.core.work.data.work.clear

import com.nuzhnov.workcontrol.core.database.dao.LessonDAO
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import javax.inject.Inject

internal class ClearLessonsWork @Inject constructor(
    private val lessonDAO: LessonDAO
) {

    suspend operator fun invoke(): Result<Unit> = safeExecute {
        lessonDAO.getFinishedEntitiesWithParticipants()
            .filter { complexEntity ->
                val isLessonSync = complexEntity.lessonEntity.isSynchronised
                val isAllParticipantsSync = complexEntity
                    .participantEntityList
                    .all { participantEntity -> participantEntity.isSynchronized }

                isLessonSync && isAllParticipantsSync
            }
            .map { complexEntity -> complexEntity.lessonEntity }
            .toTypedArray()
            .run { lessonDAO.delete(*this) }
    }
}
