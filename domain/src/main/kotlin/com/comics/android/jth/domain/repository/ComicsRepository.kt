package com.comics.android.jth.domain.repository

import com.comics.android.jth.domain.model.ApiResult
import com.comics.android.jth.domain.model.local.BookmarkItem
import com.comics.android.jth.domain.model.remote.ComicsResponse
import kotlinx.coroutines.flow.Flow

interface ComicsRepository {
    suspend fun fetchComics(page: Int, size: Int? = null, filter: String? = null): ApiResult<ComicsResponse>
    fun getBookmark(): Flow<List<BookmarkItem>>
    suspend fun addBookmark(items: List<BookmarkItem>)

    suspend fun deleteBookmark(items: List<BookmarkItem>)
}
