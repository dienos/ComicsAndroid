package com.kstd.android.jth.data.repository

import com.google.gson.Gson
import com.kstd.android.jth.data.datasource.local.source.ComicsLocalSource
import com.kstd.android.jth.data.datasource.remote.dto.ErrorResponseDto
import com.kstd.android.jth.data.datasource.remote.source.ComicsRemoteSource
import com.kstd.android.jth.data.mapper.toBookmarkEntity
import com.kstd.android.jth.domain.exception.IncorrectQueryRequestException
import com.kstd.android.jth.domain.exception.InvalidDisplayValueException
import com.kstd.android.jth.domain.exception.InvalidSearchApiException
import com.kstd.android.jth.domain.exception.InvalidSortValueException
import com.kstd.android.jth.domain.exception.InvalidStartValueException
import com.kstd.android.jth.domain.exception.MalformedEncodingException
import com.kstd.android.jth.domain.exception.SystemErrorException
import com.kstd.android.jth.domain.exception.UnknownApiException
import com.kstd.android.jth.domain.model.ApiResult
import com.kstd.android.jth.domain.model.local.BookmarkItem
import com.kstd.android.jth.domain.model.remote.ComicsResponse
import com.kstd.android.jth.domain.repository.ComicsRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ComicsRepositoryImpl @Inject constructor(
    private val localSource: ComicsLocalSource,
    private val remoteSource: ComicsRemoteSource
) : ComicsRepository {

    override suspend fun fetchComics(
        page: Int,
        size: Int?,
        filter: String?
    ): ApiResult<ComicsResponse> {
        return try {
            val response = remoteSource.fetchComics(page, size, filter)
            ApiResult.Success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = try {
                Gson().fromJson(errorBody, ErrorResponseDto::class.java)
            } catch (_: Exception) {
                null
            }

            val exception = when (errorResponse?.errorCode) {
                "SE01" -> IncorrectQueryRequestException(errorResponse.errorMessage?:"")
                "SE02" -> InvalidDisplayValueException(errorResponse.errorMessage?:"")
                "SE03" -> InvalidStartValueException(errorResponse.errorMessage?:"")
                "SE04" -> InvalidSortValueException(errorResponse.errorMessage?:"")
                "SE05" -> InvalidSearchApiException(errorResponse.errorMessage?:"")
                "SE06" -> MalformedEncodingException(errorResponse.errorMessage?:"")
                "SE99" -> SystemErrorException(errorResponse.errorMessage?:"")
                else -> UnknownApiException(errorResponse?.errorMessage ?: e.message())
            }
            ApiResult.Error(code = errorResponse?.errorCode ?: e.code().toString(), throwable = exception)
        } catch (e: IOException) {
            ApiResult.Error(code = "-1", throwable = e)
        }
    }

    override fun getBookmark(): Flow<List<BookmarkItem>> = localSource.getBookmarks()

    override suspend fun addBookmark(items: List<BookmarkItem>) {
        localSource.addBookmark(items.map { it.toBookmarkEntity() })
    }

    override suspend fun deleteBookmark(items: List<BookmarkItem>) {
        localSource.deleteBookmark(items.map { it.toBookmarkEntity() })
    }
}
