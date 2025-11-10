package com.kstd.android.jth.domain.model.local

data class BookmarkItem(
    val title: String = "",
    val link: String = "",
    val thumbnail: String = "",
    val sizeHeight: String = "",
    val sizeWidth: String = "",
    val isSelected: Boolean = false, // UI 상태 필드 추가
    val isSelectionMode: Boolean = false // UI 상태 필드 추가
)
