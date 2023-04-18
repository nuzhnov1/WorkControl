package com.nuzhnov.workcontrol.common.visitcontrol.annotation

import kotlin.annotation.AnnotationTarget.*

@MustBeDocumented
@Target(FIELD, LOCAL_VARIABLE, VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class Unique
