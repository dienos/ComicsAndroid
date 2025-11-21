package com.comics.android.jth.ui.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.comics.android.jth.databinding.ItemComicBinding
import com.comics.android.jth.domain.model.remote.ComicsItem

class ComicsAdapter(private val viewModel: ComicsViewModel) : PagingDataAdapter<ComicsItem, ComicsAdapter.ComicViewHolder>(ComicDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val binding = ItemComicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class ComicViewHolder(private val binding: ItemComicBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ComicsItem) {
            binding.item = item
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}

class ComicDiffCallback : DiffUtil.ItemCallback<ComicsItem>() {
    override fun areItemsTheSame(oldItem: ComicsItem, newItem: ComicsItem): Boolean {
        return oldItem.link == newItem.link
    }

    override fun areContentsTheSame(oldItem: ComicsItem, newItem: ComicsItem): Boolean {
        return oldItem == newItem
    }
}