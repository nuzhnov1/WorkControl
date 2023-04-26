package com.nuzhnov.workcontrol.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "discipline")
data class DisciplineEntity(
    @[PrimaryKey(autoGenerate = false) ColumnInfo(name = "discipline_id")]
    val disciplineID: Long,
    val name: String
)
