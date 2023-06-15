package com.nuzhnov.workcontrol.core.data.mapper

import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.TimeSpan


fun DateTimeTz.toLong() = local.unixMillisLong

fun Long.toDateTimeTz() = DateTime(unix = this).local

fun TimeSpan.toDouble() = milliseconds

fun Double.toTimeSpan() = TimeSpan(milliseconds = this)
