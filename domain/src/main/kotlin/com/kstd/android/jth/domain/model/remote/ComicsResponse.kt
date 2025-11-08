package com.kstd.android.jth.domain.model.remote

data class ComicsResponse(
    val lastBuildDate: String?,
    val total: Int?,
    val start: Int?,
    val display: Int?,
    val items: List<ComicsItem> = emptyList()
)
