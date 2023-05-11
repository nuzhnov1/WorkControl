package com.nuzhnov.workcontrol.core.work.domain.repository

internal interface WorkRepository {
    fun registerSyncUserDataWork()

    fun registerSyncLocalDataWork()

    fun registerClearLocalDataWork()
}
