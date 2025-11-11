package com.kstd.android.jth.ui.feature.search

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.kstd.android.jth.R
import com.kstd.android.jth.domain.model.local.BookmarkItem
import com.kstd.android.jth.domain.model.remote.ComicsItem
import com.kstd.android.jth.domain.usecase.AddBookMarkUseCase
import com.kstd.android.jth.domain.usecase.DeleteBookmarkUseCase
import com.kstd.android.jth.domain.usecase.GetBookMarkUseCase
import com.kstd.android.jth.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComicsSearchViewModel @Inject constructor(
    application: Application,
    getBookmarkUseCase: GetBookMarkUseCase,
    private val addBookmarkUseCase: AddBookMarkUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase
) : BaseViewModel(application) {

    private val _navigateToViewerEvent = MutableSharedFlow<Pair<Int, List<ComicsItem>>>()
    val navigateToViewerEvent = _navigateToViewerEvent.asSharedFlow()

    private val _allComics = MutableStateFlow<List<ComicsItem>>(emptyList())
    val searchQuery = MutableStateFlow("")

    private val rawBookmarksFlow: StateFlow<List<BookmarkItem>> = getBookmarkUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val filteredComics: StateFlow<List<ComicsItem>> = 
        combine(_allComics, searchQuery, rawBookmarksFlow) { comics, query, bookmarks ->
            val bookmarkedLinks = bookmarks.map { it.link }.toSet()
            val searchResult = if (query.isBlank()) {
                emptyList()
            } else {
                comics.filter { it.title?.contains(query, ignoreCase = true) == true }
            }
            searchResult.map { comic ->
                comic.copy(isBookmarked = bookmarkedLinks.contains(comic.link))
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setInitialComics(comics: List<ComicsItem>) {
        _allComics.value = comics
    }

    fun onSingleBookmarkClick(item: ComicsItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val link = item.link ?: return@launch
            
            val bookmarkItem = BookmarkItem(
                title = item.title ?: "",
                thumbnail = item.thumbnail ?: "",
                link = link,
                sizeHeight = item.sizeHeight ?: "",
                sizeWidth = item.sizeWidth ?: ""
            )
            if (item.isBookmarked) {
                deleteBookmarkUseCase(listOf(bookmarkItem))
                showToast(getApplication<Application>().getString(R.string.bookmark_removed))
            } else {
                addBookmarkUseCase(listOf(bookmarkItem))
                showToast(getApplication<Application>().getString(R.string.bookmark_added))
            }
        }
    }
    
    fun onItemClick(item: ComicsItem) {
        viewModelScope.launch {
            val fullList = _allComics.value
            val selectedIndex = fullList.indexOf(item)
            if (selectedIndex != -1) {
                _navigateToViewerEvent.emit(selectedIndex to fullList)
            }
        }
    }
}