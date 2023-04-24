package com.nuzhnov.workcontrol.shared.teacherservice.data.mapper

import com.nuzhnov.workcontrol.shared.teacherservice.domen.model.TeacherServiceState
import com.nuzhnov.workcontrol.shared.teacherservice.domen.model.TeacherServiceError
import com.nuzhnov.workcontrol.shared.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.shared.database.models.ParticipantActivity
import com.nuzhnov.workcontrol.common.visitcontrol.model.Visit
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerState
import com.nuzhnov.workcontrol.common.visitcontrol.control.ControlServerError


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
        lastVisit = lastVisit,
        totalVisitDuration = totalVisitDuration
    )

internal fun Iterable<ParticipantEntity>.toVisitArray(): Array<Visit> = this
    .map { participantActivity -> participantActivity.toVisit() }
    .toTypedArray()

internal fun ParticipantEntity.toVisit(): Visit =
    Visit(
        visitorID = studentID,
        isActive = isActive,
        lastVisit = lastVisit,
        totalVisitDuration = totalVisitDuration
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
