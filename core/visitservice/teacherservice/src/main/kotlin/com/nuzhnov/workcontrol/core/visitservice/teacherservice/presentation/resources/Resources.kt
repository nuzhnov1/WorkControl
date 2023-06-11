package com.nuzhnov.workcontrol.core.visitservice.teacherservice.presentation.resources

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.R
import android.content.Context


internal val Context.teacherServiceTitle
    get() = getString(R.string.teacher_service_title)


internal fun Context.generateServiceName(number: Int) = getString(R.string.service_name, number)
