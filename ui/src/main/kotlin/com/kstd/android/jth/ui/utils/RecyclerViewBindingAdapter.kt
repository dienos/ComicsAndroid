package com.kstd.android.jth.ui.utils

import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.kstd.android.jth.domain.model.local.BookmarkItem
import com.kstd.android.jth.domain.model.remote.ComicsItem
import com.kstd.android.jth.ui.feature.bookmark.BookmarkAdapter
import com.kstd.android.jth.ui.feature.home.ComicsAdapter
import com.kstd.android.jth.ui.feature.search.ComicsSearchAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@BindingAdapter("pagingItems")
fun RecyclerView.bindPagingItems(pagingDataFlow: Flow<PagingData<ComicsItem>>?) {
    val adapter = this.adapter as? ComicsAdapter ?: return

    findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
        pagingDataFlow?.collectLatest { pagingData ->
            adapter.submitData(pagingData)
        }
    }
}

@BindingAdapter("bookmarkItems")
fun RecyclerView.bindBookmarkItems(itemsFlow: Flow<List<BookmarkItem>>?) {
    val adapter = this.adapter as? BookmarkAdapter ?: return

    findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
        itemsFlow?.collectLatest { items ->
            adapter.submitList(items)
        }
    }
}

@BindingAdapter("searchItems")
fun RecyclerView.bindSearchItems(itemsFlow: Flow<List<ComicsItem>>?) {
    val adapter = this.adapter as? ComicsSearchAdapter ?: return

    findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
        itemsFlow?.collectLatest { items ->
            adapter.submitList(items)
        }
    }
}
