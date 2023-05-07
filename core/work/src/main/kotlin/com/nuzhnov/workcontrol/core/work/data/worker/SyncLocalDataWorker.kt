package com.nuzhnov.workcontrol.core.work.data.worker

import com.nuzhnov.workcontrol.core.work.data.work.complex.SyncLocalDataWork
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

internal class SyncLocalDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val syncLocalDataWork: SyncLocalDataWork,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result = withContext(context = coroutineDispatcher) {
        syncLocalDataWork().fold(
            onSuccess = { Result.success() },
            onFailure = { Result.retry() }
        )
    }
}
