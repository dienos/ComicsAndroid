package com.kstd.android.jth.ui.feature.bookmark

import com.kstd.android.jth.domain.model.local.BookmarkItem

data class SelectableBookmarkItem(
    val bookmarkItem: BookmarkItem,
    var isSelected: Boolean = false
)
