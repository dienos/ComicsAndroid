package com.kstd.android.jth.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class ComicsChannelDto(
    @SerializedName("title")
    val title: String? = null,

    @SerializedName("link")
    val link: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("lastBuildDate")
    val lastBuildDate: String? = null,

    @SerializedName("total")
    val total: Int? = null,

    @SerializedName("start")
    val start: Int? = null,

    @SerializedName("display")
    val display: Int? = null,

    @SerializedName("items")
    val items: List<ComicsItemDto> = emptyList()
)
