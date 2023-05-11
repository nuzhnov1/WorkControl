package com.nuzhnov.workcontrol.core.work.data.worker

import com.nuzhnov.workcontrol.core.work.data.work.sync.SyncUserDataWork
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted

@HiltWorker
internal class SyncUserDataWorker @Inject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val syncUserDataWork: SyncUserDataWork,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result = withContext(context = coroutineDispatcher) {
        syncUserDataWork().fold(
            onSuccess = { Result.success() },
            onFailure = { Result.retry() }
        )
    }
}
