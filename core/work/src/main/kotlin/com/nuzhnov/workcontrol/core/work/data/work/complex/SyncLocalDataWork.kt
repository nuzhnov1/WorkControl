package com.nuzhnov.workcontrol.core.work.data.work.complex

import com.nuzhnov.workcontrol.core.work.data.work.sync.*
import javax.inject.Inject

internal class SyncLocalDataWork @Inject constructor(
    private val syncBuildingsWork: SyncBuildingsWork,
    private val syncRoomsWork: SyncRoomsWork,
    private val syncFacultiesWork: SyncFacultiesWork,
    private val syncGroupsWork: SyncGroupsWork,
    private val syncStudentsWork: SyncStudentsWork,
    private val syncLessonsWork: SyncLessonsWork
) {

    suspend operator fun invoke(): Result<Unit> {
        val workResultList = listOf(
            syncLessonsWork(),
            syncStudentsWork(),
            syncGroupsWork(),
            syncFacultiesWork(),
            syncRoomsWork(),
            syncBuildingsWork()
        )

        return workResultList.firstOrNull { result -> result.isFailure } ?: Result.success(Unit)
    }
}
