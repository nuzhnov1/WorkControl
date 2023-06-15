package com.nuzhnov.workcontrol.core.works.domain.repository

internal interface WorkRepository {
    fun registerSyncUserDataWork()

    fun registerSyncLocalDataWork()

    fun registerClearLocalDataWork()
}
