package com.kstd.android.jth.data.repository

import com.google.gson.Gson
import com.kstd.android.jth.data.datasource.local.source.ComicsLocalSource
import com.kstd.android.jth.data.datasource.remote.dto.ErrorResponseDto
import com.kstd.android.jth.data.datasource.remote.source.ComicsRemoteSource
import com.kstd.android.jth.domain.model.ApiResult
import com.kstd.android.jth.domain.model.local.BookmarkItem
import com.kstd.android.jth.domain.model.remote.ComicsResponse
import com.kstd.android.jth.domain.repository.ComicsRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class FetchComicsRepositoryImpl @Inject constructor(
    private val localSource: ComicsLocalSource,
    private val remoteSource: ComicsRemoteSource
) : ComicsRepository {

    override suspend fun fetchComics(page: Int, size: Int): ApiResult<ComicsResponse> {
        return try {
            val response = remoteSource.fetchComics(page, size)
            ApiResult.Success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = try {
                Gson().fromJson(errorBody, ErrorResponseDto::class.java)
            } catch (_: Exception) {
                null
            }
            ApiResult.Error(
                code = errorResponse?.errorCode ?: e.code().toString(),
                message = errorResponse?.errorMessage ?: e.message()
            )
        } catch (e: IOException) {
            ApiResult.Error(code = "-1", message = e.message.toString())
        }
    }

    override fun getBookmark(): Flow<List<BookmarkItem>> = localSource.getBookmark()
}