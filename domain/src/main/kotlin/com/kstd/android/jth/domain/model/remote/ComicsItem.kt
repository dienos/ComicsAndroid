package com.kstd.android.jth.domain.model.remote


data class ComicsItem(
    val title: String? = null,
    val link: String? = null,
    val thumbnail: String? = null,
    val sizeHeight: String? = null,
    val sizeWidth: String? = null,
    val isBookmarked: Boolean = false
)
