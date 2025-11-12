package com.kstd.android.jth.data.datasource.local.source

import android.content.SharedPreferences
import androidx.core.content.edit
import com.kstd.android.jth.data.datasource.local.db.BookmarkDao
import com.kstd.android.jth.data.datasource.local.entity.BookmarkEntity
import com.kstd.android.jth.data.mapper.toBookmarkItems
import com.kstd.android.jth.domain.model.local.BookmarkItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ComicsLocalSource {
    fun getBookmarks(): Flow<List<BookmarkItem>>
    suspend fun addBookmark(bookmarks: List<BookmarkEntity>)
    suspend fun deleteBookmark(bookmarks: List<BookmarkEntity>)
    suspend fun isHomeGuideShown(key: String): Boolean
    suspend fun setHomeGuideShown(key: String)
    suspend fun isBookMarkGuideShown(key: String): Boolean
    suspend fun setBookMarkGuideShown(key: String)
}

class ComicsLocalSourceImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao,
    private val sharedPreferences: SharedPreferences
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

    override suspend fun isHomeGuideShown(key: String): Boolean = withContext(Dispatchers.IO) {
        sharedPreferences.getBoolean(key, true)
    }

    override suspend fun setHomeGuideShown(key: String) = withContext(Dispatchers.IO) {
        sharedPreferences.edit { putBoolean(key, false) }
    }

    override suspend fun isBookMarkGuideShown(key: String): Boolean = withContext(Dispatchers.IO) {
        sharedPreferences.getBoolean(key, true)
    }

    override suspend fun setBookMarkGuideShown(key: String) = withContext(Dispatchers.IO) {
        sharedPreferences.edit { putBoolean(key, false) }
    }
}