package com.nuzhnov.workcontrol

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.usecase.StartLessonUseCase
import android.app.Application
import javax.inject.Inject

class WorkControlApp : Application() {
    @Inject internal lateinit var service: StartLessonUseCase
}
