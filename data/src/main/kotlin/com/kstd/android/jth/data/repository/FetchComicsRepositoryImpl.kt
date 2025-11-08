package com.kstd.android.jth.data.repository

import com.kstd.android.jth.data.datasource.local.source.ComicsLocalSource
import com.kstd.android.jth.data.datasource.remote.source.ComicsRemoteSource
import com.kstd.android.jth.domain.model.ApiResult
import com.kstd.android.jth.domain.model.local.BookmarkItem
import com.kstd.android.jth.domain.model.remote.ComicsResponse
import com.kstd.android.jth.domain.repository.ComicsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchComicsRepositoryImpl @Inject constructor(
    private val localSource: ComicsLocalSource,
    private val remoteSource: ComicsRemoteSource
) : ComicsRepository {
    override suspend fun fetchComics(): ApiResult<ComicsResponse> = remoteSource.fetchComics()
    override fun getBookmark(): Flow<List<BookmarkItem>> = localSource.getBookmark()
}