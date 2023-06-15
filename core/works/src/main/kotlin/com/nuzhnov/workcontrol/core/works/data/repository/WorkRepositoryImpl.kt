package com.nuzhnov.workcontrol.core.works.data.repository

import com.nuzhnov.workcontrol.core.works.data.worker.SyncUserDataWorker
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

    private val syncUserDataWorkConstraints = Constraints.Builder()
        .setRequiredNetworkType(networkType = NetworkType.CONNECTED)
        .setRequiresDeviceIdle(true)
        .setRequiresBatteryNotLow(true)
        .build()

    private val syncUserDataWorkRequest = PeriodicWorkRequestBuilder<SyncUserDataWorker>(
        repeatInterval = SYNC_USER_DATA_WORK_TIME_INTERVAL_HOUR,
        repeatIntervalTimeUnit = TimeUnit.HOURS
    ).setConstraints(syncUserDataWorkConstraints).build()

    private val syncLocalDataWorkConstraints = Constraints.Builder()
        .setRequiredNetworkType(networkType = NetworkType.CONNECTED)
        .setRequiresDeviceIdle(true)
        .setRequiresBatteryNotLow(true)
        .build()

    private val syncLocalDataWorkRequest = PeriodicWorkRequestBuilder<SyncLocalDataWorker>(
        repeatInterval = SYNC_LOCAL_DATA_WORK_TIME_INTERVAL_HOUR,
        repeatIntervalTimeUnit = TimeUnit.HOURS
    ).setConstraints(syncLocalDataWorkConstraints).build()

    private val clearLocalDataWorkConstraints = Constraints.Builder()
        .setRequiresDeviceIdle(true)
        .setRequiresBatteryNotLow(true)
        .build()

    private val clearLocalDataWorkRequest = PeriodicWorkRequestBuilder<ClearLocalDataWorker>(
        repeatInterval = CLEAR_LOCAL_DATA_WORK_TIME_INTERVAL_HOUR,
        repeatIntervalTimeUnit = TimeUnit.HOURS
    ).setConstraints(clearLocalDataWorkConstraints).build()


    override fun registerSyncUserDataWork() {
        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
            /* uniqueWorkName = */ SYNC_USER_DATA_WORK_NAME,
            /* existingPeriodicWorkPolicy = */ ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            /* periodicWork = */ syncUserDataWorkRequest
        )
    }

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
        const val SYNC_USER_DATA_WORK_NAME =
            "Work on synchronizing user data with a remote service"

        const val SYNC_LOCAL_DATA_WORK_NAME =
            "Work on synchronizing local data with a remote service"

        const val CLEAR_LOCAL_DATA_WORK_NAME =
            "Work on cleaning up local user data"

        const val SYNC_USER_DATA_WORK_TIME_INTERVAL_HOUR = 6L
        const val SYNC_LOCAL_DATA_WORK_TIME_INTERVAL_HOUR = 24L
        const val CLEAR_LOCAL_DATA_WORK_TIME_INTERVAL_HOUR = 12L
    }
}
