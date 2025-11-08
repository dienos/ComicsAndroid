package com.kstd.android.jth.domain.usecase

import com.kstd.android.jth.domain.model.local.BookmarkItem
import com.kstd.android.jth.domain.repository.ComicsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookMarkUseCase @Inject constructor(private val repository: ComicsRepository) {
    operator fun invoke(): Flow<List<BookmarkItem>> = repository.getBookmark()
}