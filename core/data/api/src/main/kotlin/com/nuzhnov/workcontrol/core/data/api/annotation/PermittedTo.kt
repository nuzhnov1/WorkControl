package com.nuzhnov.workcontrol.core.data.api.annotation

import com.nuzhnov.workcontrol.core.models.Role

@[Target(AnnotationTarget.FUNCTION) Retention(AnnotationRetention.SOURCE)]
internal annotation class PermittedTo(vararg val user: Role)
