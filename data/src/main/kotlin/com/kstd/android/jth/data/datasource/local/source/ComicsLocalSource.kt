package com.kstd.android.jth.data.datasource.local.source

import com.kstd.android.jth.data.datasource.local.db.ComicsDao
import com.kstd.android.jth.data.mapper.toBookmarkItems
import com.kstd.android.jth.domain.model.local.BookmarkItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ComicsLocalSource {
    fun getBookmark(): Flow<List<BookmarkItem>>
}

class ComicsLocalSourceImpl @Inject constructor(
    private val dao: ComicsDao
) : ComicsLocalSource {
    override fun getBookmark(): Flow<List<BookmarkItem>> = dao.getComics().map { it.toBookmarkItems() }
}