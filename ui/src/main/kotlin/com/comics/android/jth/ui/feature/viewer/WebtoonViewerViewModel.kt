package com.comics.android.jth.ui.feature.viewer

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.collection.LruCache
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.comics.android.jth.domain.model.remote.ComicsItem
import com.comics.android.jth.ui.base.BaseViewModel
import com.comics.android.jth.ui.extension.getBitmapWithGlide
import com.comics.android.jth.ui.util.Constants.KEY_COMICS
import com.comics.android.jth.ui.util.Constants.KEY_INDEX
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    private val _readyBitmapKeys = MutableStateFlow<Set<String>>(emptySet())
    val readyBitmapKeys: StateFlow<Set<String>> = _readyBitmapKeys.asStateFlow()

    private val memoryCache: LruCache<String, Bitmap>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 4
        memoryCache = LruCache(cacheSize)

        if (_webtoonItems.value.isNotEmpty()) {
            _title.value = _webtoonItems.value[_initialIndex.value].title ?: ""
            onVisibleItemsChanged(_initialIndex.value)
        }
    }

    fun getBitmapFromCache(url: String): Bitmap? {
        return memoryCache[url]
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
        if (webtoonItems.value.isEmpty()) {
            return
        }

        val preloadRange = 15
        val start = max(0, firstVisibleItemIndex - preloadRange)
        val end = min(webtoonItems.value.size, firstVisibleItemIndex + preloadRange * 2)

        (start until end).forEach { index ->
            val item = webtoonItems.value[index]
            val url = item.link ?: return@forEach

            if (memoryCache[url] == null) {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val screenWidth = application.resources.displayMetrics.widthPixels
                        val imageWidth = item.sizeWidth?.toIntOrNull() ?: screenWidth
                        val imageHeight = item.sizeHeight?.toIntOrNull() ?: screenWidth

                        val targetHeight = (screenWidth.toFloat() / imageWidth * imageHeight).toInt()

                        val bitmap = application.getBitmapWithGlide(url, screenWidth, targetHeight)

                        memoryCache.put(url, bitmap)
                        _readyBitmapKeys.update { it + url }
                    } catch (e: Exception) {
                        Log.e("WebtoonViewerViewModel", "onVisibleItemsChanged: $e")
                    }
                }
            }
        }
    }
}
