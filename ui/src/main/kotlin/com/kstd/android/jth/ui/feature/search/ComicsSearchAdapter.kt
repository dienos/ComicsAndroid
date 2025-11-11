package com.kstd.android.jth.ui.feature.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kstd.android.jth.databinding.ItemSearchComicBinding
import com.kstd.android.jth.domain.model.remote.ComicsItem

class ComicsSearchAdapter(private val viewModel: ComicsSearchViewModel) : ListAdapter<ComicsItem, ComicsSearchAdapter.SearchViewHolder>(SearchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchComicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel)
    }

    inner class SearchViewHolder(private val binding: ItemSearchComicBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ComicsItem, viewModel: ComicsSearchViewModel) {
            binding.item = item
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}

class SearchDiffCallback : DiffUtil.ItemCallback<ComicsItem>() {
    override fun areItemsTheSame(oldItem: ComicsItem, newItem: ComicsItem): Boolean {
        return oldItem.link == newItem.link
    }

    override fun areContentsTheSame(oldItem: ComicsItem, newItem: ComicsItem): Boolean {
        return oldItem == newItem
    }
}