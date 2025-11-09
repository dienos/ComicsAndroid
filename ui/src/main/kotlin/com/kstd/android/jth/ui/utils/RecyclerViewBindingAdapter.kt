package com.kstd.android.jth.ui.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kstd.android.jth.domain.model.remote.ComicsItem
import com.kstd.android.jth.ui.feature.home.ComicsAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@BindingAdapter("pagingItems")
fun RecyclerView.bindPagingItems(pagingDataFlow: Flow<PagingData<ComicsItem>>?) {
    val adapter = this.adapter as? ComicsAdapter ?: return

    this.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
        pagingDataFlow?.collectLatest {
            adapter.submitData(it)
        }
    }
}

@BindingAdapter("imageUrl")
fun ImageView.bindImageUrl(url: String?) {
    if (url.isNullOrEmpty()) return

    Glide.with(this.context)
        .load(url)
        .into(this)
}
