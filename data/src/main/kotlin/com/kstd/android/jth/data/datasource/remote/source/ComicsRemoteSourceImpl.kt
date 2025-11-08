package com.kstd.android.jth.data.datasource.remote.source

import com.google.gson.Gson
import com.kstd.android.jth.data.datasource.remote.api.ImageApi
import com.kstd.android.jth.data.datasource.remote.dto.ErrorResponseDto
import com.kstd.android.jth.data.mapper.toComicsResponse
import com.kstd.android.jth.domain.model.ApiResult
import com.kstd.android.jth.domain.model.remote.ComicsResponse
import retrofit2.HttpException
import javax.inject.Inject

interface ComicsRemoteSource {
    suspend fun fetchComics(
        start: Int = 1,
        sort: String = "sim",
        filter: String = "all"
    ): ApiResult<ComicsResponse>
}

class ComicsRemoteSourceImpl @Inject constructor(
    private val api: ImageApi
) : ComicsRemoteSource {
    override suspend fun fetchComics(
        start: Int,
        sort: String,
        filter: String
    ): ApiResult<ComicsResponse> {
        return try {
            val response = api.fetchComics(start = start, sort = sort, filter = filter)
            ApiResult.Success(response.toComicsResponse())
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = try {
                Gson().fromJson(errorBody, ErrorResponseDto::class.java)
            } catch (e: Exception) {
                null
            }
            ApiResult.Error(
                code = errorResponse?.errorCode ?: e.code().toString(),
                message = errorResponse?.errorMessage ?: e.message()
            )
        }
    }
}