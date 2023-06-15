package com.nuzhnov.workcontrol.core.works.domain.repository

internal interface WorkRepository {
    fun registerSyncLocalDataWork()

    fun registerClearLocalDataWork()
}
