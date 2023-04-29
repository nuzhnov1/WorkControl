package com.nuzhnov.workcontrol.core.visitservice.teacherservice.data.mapper

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.model.TeacherServiceState
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.model.TeacherServiceError
import com.nuzhnov.workcontrol.core.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.database.model.ParticipantActivity
import com.nuzhnov.workcontrol.common.visitcontrol.model.Visit
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerState
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerError
import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.TimeSpan


internal fun Iterable<Visit>.toParticipantActivitiesArray(
    lessonID: Long
): Array<ParticipantActivity> = this
    .map { visit -> visit.toParticipantActivity(lessonID) }
    .toTypedArray()

internal fun Visit.toParticipantActivity(lessonID: Long): ParticipantActivity =
    ParticipantActivity(
        studentID = visitorID,
        lessonID = lessonID,
        isActive = isActive,
        lastVisit = lastVisit?.toLong(),
        totalVisitDuration = totalVisitDuration.toDouble()
    )

internal fun Iterable<ParticipantEntity>.toVisitArray(): Array<Visit> = this
    .map { participantActivity -> participantActivity.toVisit() }
    .toTypedArray()

internal fun ParticipantEntity.toVisit(): Visit =
    Visit(
        visitorID = studentID,
        isActive = isActive,
        lastVisit = lastVisit?.toDateTimeTz(),
        totalVisitDuration = totalVisitDuration.toTimeSpan()
    )

internal fun ControlServerState.toServiceState(): TeacherServiceState? = when (this) {
    is ControlServerState.NotRunningYet -> null

    is ControlServerState.Running -> TeacherServiceState.Running(address, port)

    is ControlServerState.StoppedAcceptConnections ->
        TeacherServiceState.StoppedAcceptConnections(error = error.toServiceError())

    is ControlServerState.Stopped -> TeacherServiceState.Stopped

    is ControlServerState.StoppedByError ->
        TeacherServiceState.StoppedByError(error = error.toServiceError())
}

internal fun ControlServerError.toServiceError(): TeacherServiceError = when (this) {
    ControlServerError.INIT_ERROR -> TeacherServiceError.INIT_ERROR

    ControlServerError.MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED ->
        TeacherServiceError.MAX_ACCEPT_CONNECTION_ATTEMPTS_REACHED

    ControlServerError.IO_ERROR -> TeacherServiceError.IO_ERROR
    ControlServerError.SECURITY_ERROR -> TeacherServiceError.SECURITY_ERROR
    ControlServerError.UNKNOWN_ERROR -> TeacherServiceError.UNKNOWN_ERROR
}

internal fun DateTimeTz.toLong(): Long = local.unixMillisLong

internal fun Long.toDateTimeTz(): DateTimeTz = DateTime(unix = this).local

internal fun TimeSpan.toDouble(): Double = milliseconds

internal fun Double.toTimeSpan(): TimeSpan = TimeSpan(milliseconds = this)
