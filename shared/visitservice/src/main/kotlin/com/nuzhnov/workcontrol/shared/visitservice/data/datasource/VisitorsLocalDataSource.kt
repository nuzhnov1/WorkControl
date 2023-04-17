package com.nuzhnov.workcontrol.shared.visitservice.data.datasource

import com.nuzhnov.workcontrol.shared.visitservice.data.local.dao.VisitorsDao
import com.nuzhnov.workcontrol.shared.visitservice.data.local.mapper.toModelSet
import com.nuzhnov.workcontrol.shared.visitservice.data.local.mapper.toEntityArray
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.Visitor
import com.nuzhnov.workcontrol.shared.visitservice.di.annotations.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class VisitorsLocalDataSource @Inject constructor(
    private val dao: VisitorsDao,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun getVisitors() = withContext(coroutineDispatcher) {
        dao.getVisitors().toModelSet()
    }

    suspend fun persistVisitors(visitors: Set<Visitor>) = withContext(coroutineDispatcher) {
        dao.insertOrUpdate(*visitors.toEntityArray())
    }
}
