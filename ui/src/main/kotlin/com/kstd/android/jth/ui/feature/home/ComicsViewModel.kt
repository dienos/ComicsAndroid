package com.kstd.android.jth.ui.feature.home

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.kstd.android.jth.R
import com.kstd.android.jth.domain.model.local.BookmarkItem
import com.kstd.android.jth.domain.model.remote.ComicsItem
import com.kstd.android.jth.domain.usecase.AddBookMarkUseCase
import com.kstd.android.jth.domain.usecase.DeleteBookmarkUseCase
import com.kstd.android.jth.domain.usecase.GetBookMarkUseCase
import com.kstd.android.jth.domain.usecase.FetchComicsUseCase
import com.kstd.android.jth.ui.base.BaseViewModel
import com.kstd.android.jth.ui.feature.bookmark.SelectableBookmarkItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ComicsViewModel @Inject constructor(
    application: Application,
    private val fetchComicsUseCase: FetchComicsUseCase,
    getBookmarkUseCase: GetBookMarkUseCase,
    private val addBookmarkUseCase: AddBookMarkUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase
) : BaseViewModel(application) {

    private val comicsPagingFlow = Pager(
        config = PagingConfig(
            pageSize = ComicsPagingSource.PAGE_SIZE,
            initialLoadSize = ComicsPagingSource.PAGE_SIZE,
            enablePlaceholders = false,
            prefetchDistance = 25
        ),
        pagingSourceFactory = {
            ComicsPagingSource(
                fetchComicsUseCase = fetchComicsUseCase,
                onEmpty = {
                    showToast(getApplication<Application>().getString(R.string.comics_no_results))
                },
                onError = { message ->
                    showToast(message)
                }
            )
        }
    ).flow.cachedIn(viewModelScope)

    val bookmarksFlow: StateFlow<List<BookmarkItem>> = getBookmarkUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _isBookmarkDeleteMode = MutableStateFlow(false)
    val isBookmarkDeleteMode = _isBookmarkDeleteMode.asStateFlow()

    private val _isHomeSelectionMode = MutableStateFlow(false)
    val isHomeSelectionMode = _isHomeSelectionMode.asStateFlow()

    private val _checkedHomeItems = MutableStateFlow<Map<String, ComicsItem>>(emptyMap())
    val checkedHomeItems = _checkedHomeItems.asStateFlow()

    private val _checkedBookmarkItems = MutableStateFlow<Set<String>>(emptySet())
    val checkedBookmarkItems = _checkedBookmarkItems.asStateFlow()

    val comicsFlow: Flow<PagingData<ComicsItem>> = combine(
        comicsPagingFlow,
        bookmarksFlow,
        isHomeSelectionMode,
        checkedHomeItems
    ) { pagingData, bookmarks, isSelectionMode, _ ->
        val bookmarkedLinks = bookmarks.map { it.link }.toSet()
        pagingData.map { comicItem ->
            comicItem.copy(
                isBookmarked = bookmarkedLinks.contains(comicItem.link),
                isSelectionMode = isSelectionMode
            )
        }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun onSingleBookmarkClick(item: ComicsItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val bookmarkItem = BookmarkItem(title = item.title ?: "", thumbnail = item.thumbnail ?: "", link = item.link ?: "", sizeHeight = item.sizeHeight ?: "", sizeWidth = item.sizeWidth ?: "")
            if (item.isBookmarked) {
                deleteBookmarkUseCase(listOf(bookmarkItem))
                showToast(getApplication<Application>().getString(R.string.bookmark_removed))
            } else {
                addBookmarkUseCase(listOf(bookmarkItem))
                showToast(getApplication<Application>().getString(R.string.bookmark_added))
            }
        }
    }

    fun onAddBookmarksClick() {
        viewModelScope.launch(Dispatchers.IO) {
            val itemsToBookmark = _checkedHomeItems.value.values.map { comicItem ->
                BookmarkItem(
                    title = comicItem.title ?: "",
                    link = comicItem.link ?: "",
                    thumbnail = comicItem.thumbnail ?: "",
                    sizeHeight = comicItem.sizeHeight ?: "",
                    sizeWidth = comicItem.sizeWidth ?: ""
                )
            }

            if (itemsToBookmark.isNotEmpty()) {
                addBookmarkUseCase(itemsToBookmark)
                showToast(getApplication<Application>().getString(R.string.bookmarks_added_count, itemsToBookmark.size))
            }
            
            cancelHomeSelectionMode()
        }
    }

    fun onDeleteBookmarksClick() {
        viewModelScope.launch(Dispatchers.IO) {
            val linksToDelete = _checkedBookmarkItems.value
            val itemsToDelete = bookmarksFlow.value.filter { linksToDelete.contains(it.link) }
            
            if (itemsToDelete.isNotEmpty()) {
                deleteBookmarkUseCase(itemsToDelete)
                showToast(getApplication<Application>().getString(R.string.bookmarks_deleted_count, itemsToDelete.size))
            }
            
            cancelSelectionMode()
        }
    }

    fun onHomeItemLongClick(item: ComicsItem): Boolean {
        if (!_isHomeSelectionMode.value) {
            _isHomeSelectionMode.value = true
            item.link?.let { link ->
                _checkedHomeItems.update { it + (link to item) }
            }
        }
        return true
    }

    fun onHomeItemClick(item: ComicsItem) {
        if (_isHomeSelectionMode.value) {
            item.link?.let { link ->
                _checkedHomeItems.update {
                    if (it.containsKey(link)) {
                        it - link
                    } else {
                        it + (link to item)
                    }
                }
            }
        } else {
            // TODO: 상세 화면 이동
        }
    }

    fun onTabSelected(id: Int) {
        cancelSelectionMode()
    }

    fun onBookmarkItemClick(item: BookmarkItem) {
        if (_isBookmarkDeleteMode.value) {
            _checkedBookmarkItems.update {
                if (it.contains(item.link)) {
                    it - (item.link ?: "")
                } else {
                    it + (item.link ?: "")
                }
            }
        }
    }

    fun onBookmarkItemLongClick(item: BookmarkItem): Boolean {
        if (!_isBookmarkDeleteMode.value) {
            _isBookmarkDeleteMode.value = true
            _checkedBookmarkItems.update { it + (item.link ?: "") }
            cancelHomeSelectionMode()
        }
        return true
    }

    fun refreshBookmark() {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(1000)
            _isRefreshing.value = false
        }
    }

    fun cancelSelectionMode() {
        _isBookmarkDeleteMode.value = false
        _checkedBookmarkItems.value = emptySet()
        cancelHomeSelectionMode()
    }

    private fun cancelHomeSelectionMode() {
        _isHomeSelectionMode.value = false
        _checkedHomeItems.value = emptyMap()
    }
}
