package com.kstd.android.jth.data.datasource.remote.source

import com.kstd.android.jth.data.datasource.remote.api.ImageApi
import com.kstd.android.jth.data.mapper.toComicsResponse
import com.kstd.android.jth.domain.model.remote.ComicsResponse
import javax.inject.Inject

interface ComicsRemoteSource {
    suspend fun fetchComics(page: Int, size: Int?, filter: String?): ComicsResponse
}

class ComicsRemoteSourceImpl @Inject constructor(
    private val api: ImageApi
) : ComicsRemoteSource {
    override suspend fun fetchComics(page: Int, size: Int?, filter: String?): ComicsResponse {
        return api.fetchComics(start = page, display = size, filter = filter).toComicsResponse()
    }
}
