package com.kstd.android.jth.ui.feature.home

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kstd.android.jth.domain.model.ApiResult
import com.kstd.android.jth.domain.model.remote.ComicsItem
import com.kstd.android.jth.domain.usecase.FetchComicsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

private const val TAG = "ComicsPagingSource"

class ComicsPagingSource(
    private val fetchComicsUseCase: FetchComicsUseCase,
    private val onError: (String) -> Unit,
    private val onEmpty: () -> Unit
) : PagingSource<Int, ComicsItem>() {

    companion object {
        private const val STARTING_INDEX = 1
        const val PAGE_SIZE = 50
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComicsItem> {
        val start = params.key ?: STARTING_INDEX

        return when (val result = fetchComicsUseCase(page = start, size = PAGE_SIZE)) {
            is ApiResult.Success -> {
                val comics = result.data.items

                if (comics.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        onEmpty()
                    }
                }

                comics.forEachIndexed { index, item -> 
                    val overallIndex = start + index
                    Log.d(TAG, "Item[$overallIndex]: ${item.title}")
                }

                val nextKey = if (comics.isEmpty()) {
                    null
                } else {
                    start + comics.size
                }
                
                LoadResult.Page(
                    data = comics,
                    prevKey = if (start == STARTING_INDEX) null else start - PAGE_SIZE,
                    nextKey = nextKey
                )
            }
            is ApiResult.Error -> {
                val errorMessage = "[${result.code}] ${result.message}"
                Log.e(TAG, "Error loading from start index $start: $errorMessage")

                withContext(Dispatchers.Main) {
                    onError(errorMessage)
                }

                LoadResult.Error(IOException(errorMessage))
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ComicsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(PAGE_SIZE)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(PAGE_SIZE)
        }
    }
}