package com.kstd.android.jth.ui.feature.viewer

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kstd.android.jth.domain.model.remote.ComicsItem

class WebtoonPagingSource(private val items: List<ComicsItem>) : PagingSource<Int, ComicsItem>() {
    override fun getRefreshKey(state: PagingState<Int, ComicsItem>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComicsItem> {
        val page = params.key ?: 0
        val pageSize = params.loadSize
        val start = page * pageSize
        val end = (start + pageSize).coerceAtMost(items.size)
        val sublist = items.subList(start, end)

        return LoadResult.Page(
            data = sublist,
            prevKey = if (page == 0) null else page - 1,
            nextKey = if (end == items.size) null else page + 1
        )
    }
}
