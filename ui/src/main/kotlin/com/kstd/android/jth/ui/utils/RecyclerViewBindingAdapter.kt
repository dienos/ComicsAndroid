package com.kstd.android.jth.ui.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kstd.android.jth.domain.model.remote.ComicsItem
import com.kstd.android.jth.ui.feature.home.ComicsAdapter
import com.kstd.android.jth.ui.feature.home.ComicsViewModel

@BindingAdapter("items", "viewModel")
fun RecyclerView.bindItems(items: List<ComicsItem>?, viewModel: ComicsViewModel) {
    if (items.isNullOrEmpty()) return

    val adapter = this.adapter as? ComicsAdapter ?: ComicsAdapter(viewModel).also {
        this.adapter = it
    }
    adapter.submitList(items)
}
