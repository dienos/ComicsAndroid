package com.kstd.android.jth.data.mapper

import com.google.gson.Gson
import com.kstd.android.jth.data.datasource.remote.dto.ErrorResponseDto
import com.kstd.android.jth.domain.exception.*
import retrofit2.HttpException

fun HttpException.toCustomException(): ApiException {
    val errorBody = this.response()?.errorBody()?.string()
    val errorResponse = try {
        Gson().fromJson(errorBody, ErrorResponseDto::class.java)
    } catch (e: Exception) {
        null
    }

    val errorMessage = errorResponse?.errorMessage ?: this.message()

    return when (errorResponse?.errorCode) {
        "SE01" -> IncorrectQueryRequestException(errorMessage)
        "SE02" -> InvalidDisplayValueException(errorMessage)
        "SE03" -> InvalidStartValueException(errorMessage)
        "SE04" -> InvalidSortValueException(errorMessage)
        "SE06" -> MalformedEncodingException(errorMessage)
        "SE05" -> InvalidSearchApiException(errorMessage)
        "SE99" -> SystemErrorException(errorMessage)
        else -> UnknownApiException("An unknown error occurred: ${this.code()}")
    }
}
