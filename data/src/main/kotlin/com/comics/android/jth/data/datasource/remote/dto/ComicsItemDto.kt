package com.comics.android.jth.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class ComicsItemDto(
    @SerializedName("title")
    val title: String? = null,

    @SerializedName("link")
    val link: String? = null,

    @SerializedName("thumbnail")
    val thumbnail: String? = null,

    @SerializedName("sizeheight")
    val sizeHeight: String? = null,

    @SerializedName("sizewidth")
    val sizeWidth: String? = null
)
