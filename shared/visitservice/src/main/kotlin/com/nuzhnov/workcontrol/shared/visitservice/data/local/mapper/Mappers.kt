package com.nuzhnov.workcontrol.shared.visitservice.data.local.mapper

import com.nuzhnov.workcontrol.shared.visitservice.data.local.entity.VisitorEntity
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.Visitor


internal fun Visitor.toEntity() = VisitorEntity(
    id = id,
    isActive = isActive,
    lastVisitTime = lastVisitTime,
    totalVisitDuration = totalVisitDuration
)

internal fun VisitorEntity.toModel() = Visitor(
    id = id,
    isActive = isActive,
    lastVisitTime = lastVisitTime,
    totalVisitDuration = totalVisitDuration
)

internal fun Iterable<Visitor>.toEntityArray() = map(Visitor::toEntity).toTypedArray()

internal fun Iterable<VisitorEntity>.toModelSet() = map(VisitorEntity::toModel).toSet()
