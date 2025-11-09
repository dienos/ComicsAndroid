package com.kstd.android.jth.ui.feature.home

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kstd.android.jth.R
import com.kstd.android.jth.domain.model.local.BookmarkItem
import com.kstd.android.jth.domain.model.remote.ComicsItem
import com.kstd.android.jth.domain.usecase.GetBookMarkUseCase
import com.kstd.android.jth.domain.usecase.FetchComicsUseCase
import com.kstd.android.jth.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComicsViewModel @Inject constructor(
    application: Application,
    private val fetchComicsUseCase: FetchComicsUseCase,
    private val getBookmarkUseCase: GetBookMarkUseCase
) : BaseViewModel(application) {

    val comicsFlow: Flow<PagingData<ComicsItem>> = Pager(
        config = PagingConfig(
            pageSize = ComicsPagingSource.PAGE_SIZE,
            initialLoadSize = ComicsPagingSource.PAGE_SIZE,
            enablePlaceholders = false,
            prefetchDistance = 15
        ),
        pagingSourceFactory = { 
            ComicsPagingSource(
                fetchComicsUseCase = fetchComicsUseCase, 
                onEmpty = { 
                    showToast(getApplication<Application>().getString(R.string.comics_no_results))
                }, 
                onError =  { message ->
                    showToast(message)
                }
            )
        }
    ).flow.cachedIn(viewModelScope)

    private val _bookmarkData = MutableStateFlow<List<BookmarkItem>>(emptyList())
    val bookmarkData = _bookmarkData.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _refreshEvent = MutableSharedFlow<Unit>()
    val refreshEvent = _refreshEvent.asSharedFlow()

    fun getBookmarks() {
        viewModelScope.launch {
            getBookmarkUseCase().collect {
                _bookmarkData.value = it
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _refreshEvent.emit(Unit)
            delay(1000) 
            _isRefreshing.value = false
        }
    }
}