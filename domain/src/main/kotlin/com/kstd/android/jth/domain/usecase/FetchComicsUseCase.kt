package com.kstd.android.jth.domain.usecase

import com.kstd.android.jth.domain.model.ApiResult
import com.kstd.android.jth.domain.model.remote.ComicsResponse
import com.kstd.android.jth.domain.repository.ComicsRepository
import javax.inject.Inject

class FetchComicsUseCase @Inject constructor(private val repository: ComicsRepository) {
    suspend operator fun invoke(page: Int, size: Int?, filter: String?): ApiResult<ComicsResponse> = repository.fetchComics(page, size, filter)
}