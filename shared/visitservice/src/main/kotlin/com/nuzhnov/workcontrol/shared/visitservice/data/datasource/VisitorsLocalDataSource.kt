package com.nuzhnov.workcontrol.shared.visitservice.data.datasource

import com.nuzhnov.workcontrol.shared.visitservice.data.local.dao.VisitorsDao
import com.nuzhnov.workcontrol.shared.visitservice.data.local.mapper.toModelSet
import com.nuzhnov.workcontrol.shared.visitservice.data.local.mapper.toEntityArray
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.Visitor
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class VisitorsLocalDataSource @Inject constructor(private val dao: VisitorsDao) {
    val visitors = dao.getVisitorsFlow().map { entitySet -> entitySet.toModelSet() }


    suspend fun getVisitors() = dao.getVisitors().toModelSet()

    suspend fun persistVisitors(visitors: Set<Visitor>) = dao.insertOrUpdate(
        *visitors.toEntityArray()
    )

    suspend fun clearPersistVisitors() = dao.clearVisitors()
}
