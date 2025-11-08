package com.kstd.android.jth.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class ErrorResponseDto(
    @SerializedName("errorCode")
    val errorCode: String?,
    @SerializedName("errorMessage")
    val errorMessage: String?
)
