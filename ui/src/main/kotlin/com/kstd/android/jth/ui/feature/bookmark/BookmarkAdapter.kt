package com.kstd.android.jth.ui.feature.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kstd.android.jth.databinding.ItemBookmarkBinding
import com.kstd.android.jth.domain.model.local.BookmarkItem
import com.kstd.android.jth.ui.feature.home.ComicsViewModel
import com.kstd.android.jth.ui.util.Constants

class BookmarkAdapter(private val viewModel: ComicsViewModel) : ListAdapter<BookmarkItem, BookmarkAdapter.BookmarkViewHolder>(BookmarkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding = ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it, viewModel)
        }
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val item = getItem(position)
            item?.let {
                holder.update(payloads.first() as Bundle)
            }
        }
    }

    inner class BookmarkViewHolder(private val binding: ItemBookmarkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookmarkItem, viewModel: ComicsViewModel) {
            binding.item = item
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        fun update(bundle: Bundle) {
            if (bundle.containsKey(Constants.PAYLOAD_KEY_SELECTION_MODE)) {
                val isSelectionMode = bundle.getBoolean(Constants.PAYLOAD_KEY_SELECTION_MODE)
                binding.cbBookmarkSelect.visibility = if (isSelectionMode) View.VISIBLE else View.GONE
            }
            if (bundle.containsKey(Constants.PAYLOAD_KEY_SELECTED)) {
                binding.cbBookmarkSelect.isChecked = bundle.getBoolean(Constants.PAYLOAD_KEY_SELECTED)
            }
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

    override fun getChangePayload(oldItem: BookmarkItem, newItem: BookmarkItem): Any? {
        val bundle = Bundle()
        if (oldItem.isSelectionMode != newItem.isSelectionMode) {
            bundle.putBoolean(Constants.PAYLOAD_KEY_SELECTION_MODE, newItem.isSelectionMode)
        }
        if (oldItem.isSelected != newItem.isSelected) {
            bundle.putBoolean(Constants.PAYLOAD_KEY_SELECTED, newItem.isSelected)
        }
        return if (bundle.isEmpty) null else bundle
    }
}