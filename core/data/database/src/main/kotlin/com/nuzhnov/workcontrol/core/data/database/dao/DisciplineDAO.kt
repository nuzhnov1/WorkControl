package com.nuzhnov.workcontrol.core.data.database.dao

import com.nuzhnov.workcontrol.core.data.database.entity.DisciplineEntity
import androidx.room.Dao

@Dao
abstract class DisciplineDAO : EntityDAO<DisciplineEntity>(entityName = "discipline")
