package com.nuzhnov.workcontrol.core.data.mapper

import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.TimeSpan


fun DateTimeTz.toLong(): Long = local.unixMillisLong

fun Long.toDateTimeTz(): DateTimeTz = DateTime(unix = this).local

fun TimeSpan.toDouble(): Double = milliseconds

fun Double.toTimeSpan(): TimeSpan = TimeSpan(milliseconds = this)
