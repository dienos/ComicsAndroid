package com.kstd.android.jth.data.mapper

import com.kstd.android.jth.data.datasource.local.entity.ComicsEntity
import com.kstd.android.jth.domain.model.local.BookmarkItem

fun List<ComicsEntity>.toBookmarkItems(): List<BookmarkItem> {
    return this.map { BookmarkItem(title = it.title) }
}
