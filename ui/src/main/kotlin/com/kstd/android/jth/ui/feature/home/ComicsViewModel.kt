package com.kstd.android.jth.ui.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kstd.android.jth.domain.model.ApiResult
import com.kstd.android.jth.domain.model.local.BookmarkItem
import com.kstd.android.jth.domain.model.remote.ComicsItem
import com.kstd.android.jth.domain.model.remote.ComicsResponse
import com.kstd.android.jth.domain.usecase.GetBookMarkUseCase
import com.kstd.android.jth.domain.usecase.FetchComicsUseCase
import com.kstd.android.jth.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComicsViewModel @Inject constructor(
    private val fetchComicsUseCase: FetchComicsUseCase,
    private val getBookmarkUseCase: GetBookMarkUseCase,
) : BaseViewModel() {

    private val _comicsLiveData = MutableLiveData<ComicsResponse>()
    val comicsResponse: LiveData<ComicsResponse> = _comicsLiveData

    private val _bookmarkData = MutableStateFlow<List<BookmarkItem>>(emptyList())
    val bookmarkData = _bookmarkData.asStateFlow()

    private val _errorEvent = MutableStateFlow<String?>(null)
    val errorEvent = _errorEvent.asStateFlow()

    fun fetchComics() {
        viewModelScope.launch {
            when (val result = fetchComicsUseCase.invoke()) {
                is ApiResult.Success -> {
                    _comicsLiveData.value = result.data
                }
                is ApiResult.Error -> {
                    _errorEvent.value = "[${result.code}] ${result.message}"
                }
            }
        }
    }

    fun getBookmarks() {
        viewModelScope.launch {
            getBookmarkUseCase().collect {
                _bookmarkData.value = it
            }
        }
    }

    fun onBookmarkClick(item : ComicsItem) {

    }
}