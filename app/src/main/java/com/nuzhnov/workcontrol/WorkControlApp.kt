package com.nuzhnov.workcontrol

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.usecase.StartLessonUseCase
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.usecase.DiscoverServicesUseCase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WorkControlApp : Application(), Configuration.Provider {
    @Inject internal lateinit var workerFactory: HiltWorkerFactory
    @Inject internal lateinit var startLessonUseCase: StartLessonUseCase
    @Inject internal lateinit var discoverServicesUseCase: DiscoverServicesUseCase

    override fun onCreate() {
        super.onCreate()
        Log.d("com.nuzhnov.workcontrol", startLessonUseCase.toString())
        Log.d("com.nuzhnov.workcontrol", discoverServicesUseCase.toString())
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
