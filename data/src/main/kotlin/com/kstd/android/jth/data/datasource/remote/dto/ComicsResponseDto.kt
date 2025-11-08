package com.kstd.android.jth.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class ComicsResponseDto(
    @SerializedName("lastBuildDate")
    val lastBuildDate: String?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("start")
    val start: Int?,
    @SerializedName("display")
    val display: Int?,
    @SerializedName("items")
    val items: List<ComicsItemDto> = emptyList()
)
