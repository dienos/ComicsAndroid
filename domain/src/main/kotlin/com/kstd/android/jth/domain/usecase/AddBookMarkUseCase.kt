package com.kstd.android.jth.domain.usecase

import com.kstd.android.jth.domain.model.local.BookmarkItem
import com.kstd.android.jth.domain.repository.ComicsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddBookMarkUseCase @Inject constructor(private val repository: ComicsRepository) {
    suspend operator fun invoke(items: List<BookmarkItem>) {
        repository.addBookmark(items)
    }
}
