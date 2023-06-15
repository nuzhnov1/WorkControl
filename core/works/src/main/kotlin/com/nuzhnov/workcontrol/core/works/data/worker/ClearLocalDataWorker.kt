package com.nuzhnov.workcontrol.core.works.data.worker

import com.nuzhnov.workcontrol.core.works.data.work.complex.ClearLocalDataWork
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

internal class ClearLocalDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val clearLocalDataWork: ClearLocalDataWork,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork() = withContext(context = coroutineDispatcher) {
        clearLocalDataWork().fold(
            onSuccess = { Result.success() },
            onFailure = { Result.retry() }
        )
    }
}
