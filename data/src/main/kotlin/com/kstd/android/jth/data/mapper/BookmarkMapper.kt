package com.kstd.android.jth.data.mapper

import com.kstd.android.jth.data.datasource.local.entity.BookmarkEntity
import com.kstd.android.jth.domain.model.local.BookmarkItem

fun List<BookmarkEntity>.toBookmarkItems(): List<BookmarkItem> {
    return this.map {
        BookmarkItem(
            title = it.title,
            link = it.link,
            thumbnail = it.thumbnail,
            sizeHeight = it.sizeHeight,
            sizeWidth = it.sizeWidth
        )
    }
}

fun BookmarkItem.toBookmarkEntity(): BookmarkEntity = BookmarkEntity(
    title = title,
    link = link,
    thumbnail = thumbnail,
    sizeHeight = sizeHeight,
    sizeWidth = sizeWidth
)