package com.nuzhnov.workcontrol.core.work.data.worker

import com.nuzhnov.workcontrol.core.work.data.work.complex.ClearLocalDataWork
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

internal class ClearLocalDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val clearLocalDataWork: ClearLocalDataWork
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result = clearLocalDataWork().fold(
        onSuccess = { Result.success() },
        onFailure = { Result.retry() }
    )
}
