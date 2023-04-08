package com.nuzhnov.workcontrol.shared.visitservice.data.local.entity

import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorID
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime
import org.joda.time.Duration

@Entity(tableName = "visitor")
internal data class VisitorEntity(
    @PrimaryKey val id: VisitorID,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @ColumnInfo(name = "last_visit_time") val lastVisitTime: DateTime?,
    @ColumnInfo(name = "total_visit_duration") val totalVisitDuration: Duration
)
