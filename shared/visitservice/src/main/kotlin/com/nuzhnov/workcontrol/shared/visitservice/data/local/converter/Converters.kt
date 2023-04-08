package com.nuzhnov.workcontrol.shared.visitservice.data.local.converter

import androidx.room.TypeConverter
import org.joda.time.DateTime
import org.joda.time.Duration

internal object Converters {
    @TypeConverter
    fun dateTimeFromLong(value: Long?) = value?.let { DateTime(it) }

    @TypeConverter
    fun dateTimeToLong(value: DateTime?) = value?.millis

    @TypeConverter
    fun durationFromLong(value: Long) = Duration(value)

    @TypeConverter
    fun durationToLong(value: Duration) = value.millis
}
