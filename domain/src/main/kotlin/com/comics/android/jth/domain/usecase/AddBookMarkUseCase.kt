package com.comics.android.jth.domain.usecase

import com.comics.android.jth.domain.model.local.BookmarkItem
import com.comics.android.jth.domain.repository.ComicsRepository
import javax.inject.Inject

class AddBookMarkUseCase @Inject constructor(private val repository: ComicsRepository) {
    suspend operator fun invoke(items: List<BookmarkItem>) {
        repository.addBookmark(items)
    }
}
