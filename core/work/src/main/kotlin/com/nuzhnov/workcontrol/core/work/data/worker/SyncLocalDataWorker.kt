package com.nuzhnov.workcontrol.core.work.data.worker

import com.nuzhnov.workcontrol.core.work.data.work.complex.SyncLocalDataWork
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

internal class SyncLocalDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val syncLocalDataWork: SyncLocalDataWork
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result = syncLocalDataWork().fold(
        onSuccess = { Result.success() },
        onFailure = { Result.retry() }
    )
}
