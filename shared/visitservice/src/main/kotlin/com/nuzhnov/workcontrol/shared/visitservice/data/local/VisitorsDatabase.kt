package com.nuzhnov.workcontrol.shared.visitservice.data.local

import com.nuzhnov.workcontrol.shared.visitservice.data.local.converter.Converters
import com.nuzhnov.workcontrol.shared.visitservice.data.local.entity.VisitorEntity
import com.nuzhnov.workcontrol.shared.visitservice.data.local.dao.VisitorsDao
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [VisitorEntity::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class VisitorsDatabase : RoomDatabase() {
    abstract fun provideVisitorsDao(): VisitorsDao
}
