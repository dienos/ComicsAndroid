package com.comics.android.jth.ui.feature.home

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.comics.android.jth.R
import com.comics.android.jth.domain.model.local.BookmarkItem
import com.comics.android.jth.domain.model.remote.ComicsItem
import com.comics.android.jth.domain.usecase.AddBookMarkUseCase
import com.comics.android.jth.domain.usecase.DeleteBookmarkUseCase
import com.comics.android.jth.domain.usecase.GetBookMarkUseCase
import com.comics.android.jth.domain.usecase.FetchComicsUseCase
import com.comics.android.jth.domain.usecase.IsBookMarkGuideShownUseCase
import com.comics.android.jth.domain.usecase.IsHomeGuideShownUseCase
import com.comics.android.jth.domain.usecase.SetBookMarkGuideShownUseCase
import com.comics.android.jth.domain.usecase.SetHomeGuideShownUseCase
import com.comics.android.jth.ui.base.BaseViewModel
import com.comics.android.jth.ui.extension.getImageFilterByResolution
import com.comics.android.jth.ui.util.PrefKey.IS_BOOK_MARK_GUIDE_SHOWN
import com.comics.android.jth.ui.util.PrefKey.IS_HOME_GUIDE_SHOWN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
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
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    private val isHomeGuideShownUseCase: IsHomeGuideShownUseCase,
    private val setHomeGuideShownUseCase: SetHomeGuideShownUseCase,
    private val isBookMarkGuideShownUseCase: IsBookMarkGuideShownUseCase,
    private val setBookMarkGuideShownUseCase: SetBookMarkGuideShownUseCase
) : BaseViewModel(application) {

    private val _navigateToViewerEvent = MutableSharedFlow<Pair<Int, List<ComicsItem>>>()
    val navigateToViewerEvent = _navigateToViewerEvent.asSharedFlow()

    var currentComicsList: List<ComicsItem> = emptyList()

    private val _isHomeGuideShown = MutableStateFlow(isHomeGuideShownUseCase(IS_HOME_GUIDE_SHOWN))
    val isHomeGuideShown = _isHomeGuideShown.asStateFlow()

    private val _isBookMarkGuideShown = MutableStateFlow(isBookMarkGuideShownUseCase(IS_BOOK_MARK_GUIDE_SHOWN))
    val isBookMarkGuideShown = _isBookMarkGuideShown.asStateFlow()

    private val rawBookmarksFlow: StateFlow<List<BookmarkItem>> = getBookmarkUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val isBookmarkListEmpty: StateFlow<Boolean> = rawBookmarksFlow.map { it.isEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    fun updateCurrentComicsList(comics: List<ComicsItem>) {
        currentComicsList = comics
    }

    private val comicsPagingFlow = Pager(
        config = PagingConfig(
            pageSize = ComicsPagingSource.Companion.PAGE_SIZE,
            initialLoadSize = ComicsPagingSource.Companion.PAGE_SIZE,
            enablePlaceholders = false,
            prefetchDistance = 25
        ),
        pagingSourceFactory = {
            ComicsPagingSource(
                fetchComicsUseCase = fetchComicsUseCase,
                onEmpty = {
                    showToast(getApplication<Application>().getString(R.string.comics_no_results))
                },
                onError = { throwable ->
                    showToast(
                        throwable.message
                            ?: getApplication<Application>().getString(R.string.unknown_error)
                    )
                }, filter = application.getImageFilterByResolution()
            )
        }
    ).flow.cachedIn(viewModelScope)

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
        rawBookmarksFlow,
        isHomeSelectionMode,
        checkedHomeItems
    ) { pagingData, bookmarks, isSelectionMode, _ ->
        val bookmarkedLinks = bookmarks.map { it.link }.toSet()
        pagingData.map { comicItem ->
            comicItem.copy(
                isBookmarked = bookmarkedLinks.contains(comicItem.link),
                isSelectionMode = isSelectionMode,
                isChecked = checkedHomeItems.value.contains(comicItem.link),
            )
        }
    }

    val bookmarksFlow: Flow<List<BookmarkItem>> = combine(
        rawBookmarksFlow,
        isBookmarkDeleteMode,
        checkedBookmarkItems
    ) { bookmarks, isSelectionMode, checkedItems ->
        bookmarks.map {
            it.copy(
                isSelectionMode = isSelectionMode,
                isSelected = checkedItems.contains(it.link)
            )
        }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun onSingleBookmarkClick(item: ComicsItem) {
        viewModelScope.launch {
            val bookmarkItem = BookmarkItem(
                title = item.title ?: "",
                thumbnail = item.thumbnail ?: "",
                link = item.link ?: "",
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

    fun onAddBookmarksClick() {
        viewModelScope.launch {
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
                showToast(
                    getApplication<Application>().getString(
                        R.string.bookmarks_added_count,
                        itemsToBookmark.size
                    )
                )
            }

            cancelHomeSelectionMode()
        }
    }

    fun onDeleteBookmarksClick() {
        viewModelScope.launch {
            val itemsToDelete =
                rawBookmarksFlow.value.filter { _checkedBookmarkItems.value.contains(it.link) }

            if (itemsToDelete.isNotEmpty()) {
                deleteBookmarkUseCase(itemsToDelete)
                showToast(
                    getApplication<Application>().getString(
                        R.string.bookmarks_deleted_count,
                        itemsToDelete.size
                    )
                )
            }

            cancelSelectionMode()
        }
    }

    fun onHomeGuideTouch() {
        viewModelScope.launch {
            setHomeGuideShownUseCase.invoke(IS_HOME_GUIDE_SHOWN, true)
            _isHomeGuideShown.value = true
        }
    }

    fun onBookMarkGuideTouch() {
        viewModelScope.launch {
            setBookMarkGuideShownUseCase.invoke(IS_BOOK_MARK_GUIDE_SHOWN, true)
            _isBookMarkGuideShown.value = true
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
            viewModelScope.launch {
                val selectedIndex = currentComicsList.indexOfFirst { it.link == item.link }

                if (selectedIndex != -1) {
                    _navigateToViewerEvent.emit(selectedIndex to currentComicsList)
                }
            }
        }
    }

    fun onTabSelected() {
        cancelSelectionMode()
    }

    fun onBookmarkItemClick(item: BookmarkItem) {
        if (_isBookmarkDeleteMode.value) {
            _checkedBookmarkItems.update {
                if (it.contains(item.link)) {
                    it - item.link
                } else {
                    it + item.link
                }
            }
        } else {
            viewModelScope.launch {
                val selectedIndex = currentComicsList.indexOfFirst { it.link == item.link }

                if (selectedIndex != -1) {
                    _navigateToViewerEvent.emit(selectedIndex to currentComicsList)
                } else {
                    showToast(getApplication<Application>().getString(R.string.comics_not_loaded))
                }
            }
        }
    }

    fun onBookmarkItemLongClick(item: BookmarkItem): Boolean {
        if (!_isBookmarkDeleteMode.value) {
            _isBookmarkDeleteMode.value = true
            _checkedBookmarkItems.update { it + item.link }
            cancelHomeSelectionMode()
        }
        return true
    }

    fun refreshBookmark() {
        viewModelScope.launch {
            cancelSelectionMode()
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
