package com.nuzhnov.workcontrol.core.work.domain.repository

internal interface WorkRepository {
    fun registerSyncLocalDataWork()
    fun registerClearLocalDataWork()
}
