package com.comics.android.jth.domain.usecase

import com.comics.android.jth.domain.model.local.BookmarkItem
import com.comics.android.jth.domain.repository.ComicsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookMarkUseCase @Inject constructor(private val repository: ComicsRepository) {
    operator fun invoke(): Flow<List<BookmarkItem>> = repository.getBookmark()
}