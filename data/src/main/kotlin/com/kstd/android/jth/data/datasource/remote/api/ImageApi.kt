package com.kstd.android.jth.data.datasource.remote.api

import com.kstd.android.jth.data.datasource.remote.dto.ComicsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {
    @GET("v1/search/image")
    suspend fun fetchComics(
        @Query("query") query: String = "만화",
        @Query("display") display: Int = 50,
        @Query("start") start: Int = 1,
        @Query("sort") sort: String = "sim",
        @Query("filter") filter: String = "all"
    ): ComicsResponseDto
}