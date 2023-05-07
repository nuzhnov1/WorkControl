package com.nuzhnov.workcontrol.core.api.annotation

import com.nuzhnov.workcontrol.core.model.Role

@[Target(AnnotationTarget.FUNCTION) Retention(AnnotationRetention.SOURCE)]
internal annotation class PermittedTo(vararg val user: Role)
