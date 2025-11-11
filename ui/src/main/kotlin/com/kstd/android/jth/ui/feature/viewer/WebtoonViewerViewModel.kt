package com.kstd.android.jth.ui.feature.viewer

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kstd.android.jth.domain.model.remote.ComicsItem
import com.kstd.android.jth.ui.base.BaseViewModel
import com.kstd.android.jth.ui.extension.preloadWebtoonImages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WebtoonViewerViewModel @Inject constructor(
    private val application: Application
) : BaseViewModel(application) {

    private val _webtoonFlow = MutableStateFlow<Flow<PagingData<ComicsItem>>?>(null)
    val webtoonFlow: StateFlow<Flow<PagingData<ComicsItem>>?> = _webtoonFlow.asStateFlow()

    private val _initialIndex = MutableStateFlow(0)
    val initialIndex: StateFlow<Int> = _initialIndex.asStateFlow()

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    fun setWebtoonData(comics: List<ComicsItem>, index: Int) {
        _initialIndex.value = index
        if (comics.isNotEmpty()) {
            _title.value = comics[index].title ?: ""
            val preloadStartIndex = index
            val preloadEndIndex = (index + 15).coerceAtMost(comics.size)
            val sublistToPreload = comics.subList(preloadStartIndex, preloadEndIndex)
            preloadWebtoonImages(application.applicationContext, sublistToPreload, viewModelScope)
        }

        _webtoonFlow.value = Pager(
            config = PagingConfig(
                pageSize = 30,
                prefetchDistance = 10,
                initialLoadSize = 30
            ),
            pagingSourceFactory = { WebtoonPagingSource(comics) }
        ).flow.cachedIn(viewModelScope)
    }
}
