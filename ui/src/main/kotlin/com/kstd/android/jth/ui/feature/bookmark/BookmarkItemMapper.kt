package com.kstd.android.jth.ui.feature.bookmark

import com.kstd.android.jth.domain.model.local.BookmarkItem

fun List<BookmarkItem>.toSelectableItems(): List<SelectableBookmarkItem> {
    return this.map { it.toSelectable() }
}

fun BookmarkItem.toSelectable(): SelectableBookmarkItem {
    return SelectableBookmarkItem(bookmarkItem = this)
}
