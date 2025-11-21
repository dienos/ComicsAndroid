package com.comics.android.jth.domain.usecase

import com.comics.android.jth.domain.model.ApiResult
import com.comics.android.jth.domain.model.remote.ComicsResponse
import com.comics.android.jth.domain.repository.ComicsRepository
import javax.inject.Inject

class FetchComicsUseCase @Inject constructor(private val repository: ComicsRepository) {
    suspend operator fun invoke(page: Int, size: Int?, filter: String?): ApiResult<ComicsResponse> = repository.fetchComics(page, size, filter)
}