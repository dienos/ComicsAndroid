package com.kstd.android.jth.ui.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kstd.android.jth.databinding.ItemComicBinding
import com.kstd.android.jth.domain.model.remote.ComicsItem
import com.kstd.android.jth.ui.util.Constants

class ComicsAdapter(private val viewModel: ComicsViewModel) : PagingDataAdapter<ComicsItem, ComicsAdapter.ViewHolder>(ComicDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemComicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, viewModel)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.update(payloads.first() as Bundle)
        }
    }

    inner class ViewHolder(private val binding: ItemComicBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ComicsItem, viewModel: ComicsViewModel) {
            binding.item = item
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        fun update(bundle: Bundle) {
            if (bundle.containsKey(Constants.PAYLOAD_KEY_BOOKMARK)) {
                binding.tbBookmark.isChecked = bundle.getBoolean(Constants.PAYLOAD_KEY_BOOKMARK)
            }
            if (bundle.containsKey(Constants.PAYLOAD_KEY_CHECK)) {
                binding.cbHomeSelect.isChecked = bundle.getBoolean(Constants.PAYLOAD_KEY_CHECK)
            }
            if (bundle.containsKey(Constants.PAYLOAD_KEY_SELECTION_MODE)) {
                val isSelectionMode = bundle.getBoolean(Constants.PAYLOAD_KEY_SELECTION_MODE)
                binding.tbBookmark.visibility = if (!isSelectionMode) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
                val isBookmarked = binding.item?.isBookmarked ?: false
                binding.cbHomeSelect.visibility = if (isSelectionMode && !isBookmarked) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
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

    override fun getChangePayload(oldItem: ComicsItem, newItem: ComicsItem): Any? {
        val bundle = Bundle()
        if (oldItem.isBookmarked != newItem.isBookmarked) {
            bundle.putBoolean(Constants.PAYLOAD_KEY_BOOKMARK, newItem.isBookmarked)
        }
        if (oldItem.isChecked != newItem.isChecked) {
            bundle.putBoolean(Constants.PAYLOAD_KEY_CHECK, newItem.isChecked)
        }
        if (oldItem.isSelectionMode != newItem.isSelectionMode) {
            bundle.putBoolean(Constants.PAYLOAD_KEY_SELECTION_MODE, newItem.isSelectionMode)
        }

        return if (bundle.isEmpty) {
            null
        } else {
            bundle
        }
    }
}