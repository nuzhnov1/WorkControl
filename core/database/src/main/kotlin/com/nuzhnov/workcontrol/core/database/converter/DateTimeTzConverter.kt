package com.nuzhnov.workcontrol.core.database.converter

import androidx.room.TypeConverter
import com.soywiz.klock.DateTimeTz

internal object DateTimeTzConverter {
    @TypeConverter
    fun fromDateTimeTz(value: DateTimeTz?): Long? = value?.run {
        local.unixMillisLong
    }

    @TypeConverter
    fun toDateTimeTz(value: Long?): DateTimeTz? = value?.run {
        DateTimeTz.fromUnixLocal(unix = this)
    }
}
