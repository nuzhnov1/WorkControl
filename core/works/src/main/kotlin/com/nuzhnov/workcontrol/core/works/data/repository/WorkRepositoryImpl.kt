package com.nuzhnov.workcontrol.core.works.data.repository

import com.nuzhnov.workcontrol.core.works.data.worker.SyncLocalDataWorker
import com.nuzhnov.workcontrol.core.works.data.worker.ClearLocalDataWorker
import com.nuzhnov.workcontrol.core.works.domain.repository.WorkRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import android.content.Context
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext

internal class WorkRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
) : WorkRepository {

    private val syncLocalDataWorkConstraints = Constraints.Builder()
        .setRequiredNetworkType(networkType = NetworkType.CONNECTED)
        .setRequiresDeviceIdle(true)
        .setRequiresBatteryNotLow(true)
        .build()

    private val syncLocalDataWorkRequest = PeriodicWorkRequestBuilder<SyncLocalDataWorker>(
        repeatInterval = SYNC_LOCAL_DATA_WORK_TIME_INTERVAL_HOURS,
        repeatIntervalTimeUnit = TimeUnit.HOURS
    ).setConstraints(syncLocalDataWorkConstraints).build()

    private val clearLocalDataWorkConstraints = Constraints.Builder()
        .setRequiresDeviceIdle(true)
        .setRequiresBatteryNotLow(true)
        .build()

    private val clearLocalDataWorkRequest = PeriodicWorkRequestBuilder<ClearLocalDataWorker>(
        repeatInterval = CLEAR_LOCAL_DATA_WORK_TIME_INTERVAL_DAYS,
        repeatIntervalTimeUnit = TimeUnit.DAYS
    ).setConstraints(clearLocalDataWorkConstraints).build()


    override fun registerSyncLocalDataWork() {
        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
            /* uniqueWorkName = */ SYNC_LOCAL_DATA_WORK_NAME,
            /* existingPeriodicWorkPolicy = */ ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            /* periodicWork = */ syncLocalDataWorkRequest
        )
    }

    override fun registerClearLocalDataWork() {
        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
            /* uniqueWorkName = */ CLEAR_LOCAL_DATA_WORK_NAME,
            /* existingPeriodicWorkPolicy = */ ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            /* periodicWork = */ clearLocalDataWorkRequest
        )
    }


    private companion object {
        const val SYNC_LOCAL_DATA_WORK_NAME = "com.nuzhnov.workcontrol.core.works.data.repository.SYNC_LOCAL_DATA_WORK_NAME"
        const val CLEAR_LOCAL_DATA_WORK_NAME = "com.nuzhnov.workcontrol.core.works.data.repository.CLEAR_LOCAL_DATA_WORK_NAME"

        const val SYNC_LOCAL_DATA_WORK_TIME_INTERVAL_HOURS = 24L
        const val CLEAR_LOCAL_DATA_WORK_TIME_INTERVAL_DAYS = 7L
    }
}
