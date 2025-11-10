package com.kstd.android.jth.data.datasource.local.source

import com.kstd.android.jth.data.datasource.local.db.BookmarkDao
import com.kstd.android.jth.data.datasource.local.entity.BookmarkEntity
import com.kstd.android.jth.data.mapper.toBookmarkItems
import com.kstd.android.jth.domain.model.local.BookmarkItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ComicsLocalSource {
    fun getBookmarks(): Flow<List<BookmarkItem>>
    suspend fun addBookmark(bookmarks: List<BookmarkEntity>)
    suspend fun deleteBookmark(bookmarks: List<BookmarkEntity>)
}

class ComicsLocalSourceImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : ComicsLocalSource {

    override fun getBookmarks(): Flow<List<BookmarkItem>> = bookmarkDao.getBookmarks().map {
        it.toBookmarkItems()
    }

    override suspend fun addBookmark(bookmarks: List<BookmarkEntity>) {
        bookmarkDao.insert(bookmarks)
    }

    override suspend fun deleteBookmark(bookmarks: List<BookmarkEntity>) {
        bookmarkDao.delete(bookmarks)
    }
}
