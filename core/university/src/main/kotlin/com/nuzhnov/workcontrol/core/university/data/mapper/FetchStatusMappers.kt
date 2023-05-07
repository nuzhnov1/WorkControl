package com.nuzhnov.workcontrol.core.university.data.mapper

import com.nuzhnov.workcontrol.core.university.domen.model.FetchStatus
import com.nuzhnov.workcontrol.core.api.util.Response


internal fun Response.Failure.toFailureFetchStatus(): FetchStatus.Failure = when (this) {
    is Response.Failure.HttpClientError.TooManyRequests -> FetchStatus.Failure.TooManyRequests
    is Response.Failure.HttpServerError -> FetchStatus.Failure.ServiceError
    is Response.Failure.NetworkError -> FetchStatus.Failure.NetworkError
    else -> FetchStatus.Failure.UnknownError
}
