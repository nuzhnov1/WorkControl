package com.nuzhnov.workcontrol.core.works.data.work.complex

import com.nuzhnov.workcontrol.core.works.data.work.sync.*
import javax.inject.Inject

internal class SyncLocalDataWork @Inject constructor(
    private val syncLessonsWork: SyncLessonsWork
) {

    suspend operator fun invoke(): Result<Unit> {
        val workResultList = listOf(syncLessonsWork())
        return workResultList.firstOrNull { result -> result.isFailure } ?: Result.success(Unit)
    }
}
