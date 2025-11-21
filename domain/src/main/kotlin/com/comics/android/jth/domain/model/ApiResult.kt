package com.comics.android.jth.domain.model

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val code: String, val throwable: Throwable) : ApiResult<Nothing>()
}
