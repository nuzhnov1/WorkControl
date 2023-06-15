package com.nuzhnov.workcontrol.core.util.roles

import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.data.preferences.model.Session
import com.nuzhnov.workcontrol.core.models.Role
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalStateException



suspend fun <T> AppPreferences.requireRole(role: Role, flow: Flow<T>): Flow<T> {
    val session = getSession()

    return if (session?.role == role) {
        flow
    } else {
        throw PermissionDeniedException
    }
}

suspend fun <T> AppPreferences.requireTeacherRole(flow: Flow<T>) =
    requireRole(role = Role.TEACHER, flow)

suspend fun <T> AppPreferences.requireStudentRole(flow: Flow<T>) =
    requireRole(role = Role.STUDENT, flow)


suspend inline fun <T> AppPreferences.requireRole(
    role: Role,
    crossinline flowBuilder: suspend (Session) -> Flow<T>
): Flow<T> {
    val session = getSession()

    return if (session?.role == role) {
        flowBuilder(session)
    } else {
        throw PermissionDeniedException
    }
}

suspend inline fun <T> AppPreferences.requireTeacherRole(
    crossinline flowBuilder: suspend (Session) -> Flow<T>
) = requireRole(role = Role.TEACHER, flowBuilder)

suspend inline fun <T> AppPreferences.requireStudentRole(
    crossinline flowBuilder: suspend (Session) -> Flow<T>
) = requireRole(role = Role.STUDENT, flowBuilder)


suspend fun AppPreferences.requireRole(role: Role): Session {
    val session = getSession()

    return if (session?.role == role) {
        session
    } else {
        throw PermissionDeniedException
    }
}

suspend fun AppPreferences.requireTeacherRole() = requireRole(Role.TEACHER)

suspend fun AppPreferences.requireStudentRole() = requireRole(Role.STUDENT)



object PermissionDeniedException : IllegalStateException()
