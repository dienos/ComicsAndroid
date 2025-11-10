package com.kstd.android.jth.domain.usecase

import com.kstd.android.jth.domain.model.local.BookmarkItem
import com.kstd.android.jth.domain.repository.ComicsRepository
import javax.inject.Inject

class DeleteBookmarkUseCase @Inject constructor(private val repository: ComicsRepository) {
    suspend operator fun invoke(items: List<BookmarkItem>) {
        repository.deleteBookmark(items)
    }
}
