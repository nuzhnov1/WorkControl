package com.nuzhnov.workcontrol.core.lesson.data.worker

import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

internal class FinalizerWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }
}
