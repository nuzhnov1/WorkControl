package com.nuzhnov.workcontrol.core.database.converter

import androidx.room.TypeConverter
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.TimeSpan

internal object Converters {
    @TypeConverter
    fun dateTimeTzToLong(value: DateTimeTz?): Long? = value?.local?.unixMillisLong

    @TypeConverter
    fun longToDateTimeTz(value: Long?): DateTimeTz? = value?.run {
        DateTimeTz.fromUnixLocal(unix = this)
    }

    @TypeConverter
    fun timeSpanToDouble(value: TimeSpan): Double = value.milliseconds

    @TypeConverter
    fun doubleToTimeSpan(value: Double): TimeSpan = TimeSpan(milliseconds = value)
}
