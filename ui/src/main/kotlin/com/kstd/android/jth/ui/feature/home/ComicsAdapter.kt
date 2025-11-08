package com.kstd.android.jth.ui.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kstd.android.jth.databinding.ItemComicBinding
import com.kstd.android.jth.domain.model.remote.ComicsItem

class ComicsAdapter(private val viewModel: ComicsViewModel) : ListAdapter<ComicsItem, ComicsAdapter.ComicViewHolder>(ComicDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val binding = ItemComicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.bind(getItem(position))
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