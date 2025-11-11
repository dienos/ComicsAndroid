package com.kstd.android.jth.ui.feature.viewer

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.kstd.android.jth.domain.model.remote.ComicsItem
import com.kstd.android.jth.ui.base.BaseViewModel
import com.kstd.android.jth.ui.extension.preloadWebtoonImages
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class WebtoonViewerViewModel @Inject constructor(
    private val application: Application
) : BaseViewModel(application) {

    private val _webtoonItems = MutableStateFlow<List<ComicsItem>>(emptyList())
    val webtoonItems: StateFlow<List<ComicsItem>> = _webtoonItems.asStateFlow()

    private val _initialIndex = MutableStateFlow(0)
    val initialIndex: StateFlow<Int> = _initialIndex.asStateFlow()

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private var lastPreloadedIndex = -1

    fun setWebtoonData(comics: List<ComicsItem>, index: Int) {
        _initialIndex.value = index
        _webtoonItems.value = comics

        if (comics.isNotEmpty()) {
            _title.value = comics[index].title ?: ""
            onVisibleItemsChanged(index)
        }
    }

    fun onVisibleItemsChanged(firstVisibleItemIndex: Int) {
        if (webtoonItems.value.isEmpty() || firstVisibleItemIndex == lastPreloadedIndex) {
            return
        }

        lastPreloadedIndex = firstVisibleItemIndex

        val preloadRange = 30
        val start = max(0, firstVisibleItemIndex - preloadRange)
        val end = min(webtoonItems.value.size, firstVisibleItemIndex + preloadRange)

        if (start < end) {
            val sublistToPreload = webtoonItems.value.subList(start, end)
            preloadWebtoonImages(application.applicationContext, sublistToPreload, viewModelScope)
        }
    }
}
