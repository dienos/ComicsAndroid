package com.comics.android.jth.domain.model.local

data class BookmarkItem(
    val title: String = "",
    val link: String = "",
    val thumbnail: String = "",
    val sizeHeight: String = "",
    val sizeWidth: String = "",
    val isSelected: Boolean = false,
    val isSelectionMode: Boolean = false
)
