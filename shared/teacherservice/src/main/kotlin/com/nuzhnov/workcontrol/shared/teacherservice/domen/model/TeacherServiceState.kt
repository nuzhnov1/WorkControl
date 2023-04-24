package com.nuzhnov.workcontrol.shared.teacherservice.domen.model

import java.net.InetAddress

sealed interface TeacherServiceState {
    object NotInitialized : TeacherServiceState
    object InitFailed : TeacherServiceState
    object ReadyToRun : TeacherServiceState

    data class Running internal constructor(
        internal val serverAddress: InetAddress,
        internal val serverPort: Int
    ) : TeacherServiceState

    data class StoppedAcceptConnections(val error: TeacherServiceError) : TeacherServiceState
    object Stopped : TeacherServiceState
    data class StoppedByError(val error: TeacherServiceError) : TeacherServiceState
}
