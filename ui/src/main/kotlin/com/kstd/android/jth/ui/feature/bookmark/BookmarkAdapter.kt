package com.kstd.android.jth.ui.feature.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kstd.android.jth.databinding.ItemBookmarkBinding
import com.kstd.android.jth.domain.model.local.BookmarkItem
import com.kstd.android.jth.ui.feature.home.ComicsViewModel

class BookmarkAdapter(private val viewModel: ComicsViewModel) : ListAdapter<BookmarkItem, BookmarkAdapter.BookmarkViewHolder>(BookmarkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding = ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    inner class BookmarkViewHolder(private val binding: ItemBookmarkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookmarkItem) {
            binding.item = item
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}

class BookmarkDiffCallback : DiffUtil.ItemCallback<BookmarkItem>() {
    override fun areItemsTheSame(oldItem: BookmarkItem, newItem: BookmarkItem): Boolean {
        return oldItem.link == newItem.link
    }

    override fun areContentsTheSame(oldItem: BookmarkItem, newItem: BookmarkItem): Boolean {
        return oldItem == newItem
    }
}