package com.kstd.android.jth.ui.feature.viewer

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kstd.android.jth.domain.model.remote.ComicsItem
import com.kstd.android.jth.ui.base.BaseViewModel
import com.kstd.android.jth.ui.extension.preloadWebtoonImages
import com.kstd.android.jth.ui.util.Constants.KEY_COMICS
import com.kstd.android.jth.ui.util.Constants.KEY_INDEX
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class WebtoonViewerViewModel @Inject constructor(
    private val application: Application,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel(application) {

    private val _webtoonItems = savedStateHandle.getStateFlow(KEY_COMICS, emptyList<ComicsItem>())
    val webtoonItems: StateFlow<List<ComicsItem>> = _webtoonItems

    private val _initialIndex = savedStateHandle.getStateFlow(KEY_INDEX, 0)
    val initialIndex: StateFlow<Int> = _initialIndex

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private var lastPreloadedIndex = -1

    init {
        if (_webtoonItems.value.isNotEmpty()) {
            _title.value = _webtoonItems.value[_initialIndex.value].title ?: ""
            onVisibleItemsChanged(_initialIndex.value)
        }
    }

    fun setWebtoonData(comics: List<ComicsItem>, index: Int) {
        if (savedStateHandle.get<List<ComicsItem>>(KEY_COMICS).isNullOrEmpty()) {
            savedStateHandle[KEY_COMICS] = comics
            savedStateHandle[KEY_INDEX] = index

            if (comics.isNotEmpty()) {
                _title.value = comics[index].title ?: ""
                onVisibleItemsChanged(index)
            }
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
