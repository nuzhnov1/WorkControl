package com.nuzhnov.workcontrol.core.university.data.mapper

import com.nuzhnov.workcontrol.core.university.domen.model.LoadStatus
import com.nuzhnov.workcontrol.core.api.util.Response


internal fun <T> Response<T>.toLoadStatus(): LoadStatus = when (this) {
    is Response.Success -> LoadStatus.Success
    is Response.Failure.HttpClientError.TooManyRequests -> LoadStatus.Failure.TooManyRequests
    is Response.Failure.HttpServerError -> LoadStatus.Failure.ServiceError
    is Response.Failure.NetworkError -> LoadStatus.Failure.NetworkError
    else -> LoadStatus.Failure.UnknownError
}
