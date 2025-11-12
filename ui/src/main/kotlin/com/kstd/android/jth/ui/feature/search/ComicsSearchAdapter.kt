package com.kstd.android.jth.ui.feature.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kstd.android.jth.databinding.ItemSearchComicBinding
import com.kstd.android.jth.domain.model.remote.ComicsItem
import com.kstd.android.jth.ui.util.Constants

class ComicsSearchAdapter(private val viewModel: ComicsSearchViewModel) : ListAdapter<ComicsItem, ComicsSearchAdapter.SearchViewHolder>(SearchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchComicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.update(payloads.first() as Bundle)
        }
    }

    inner class SearchViewHolder(private val binding: ItemSearchComicBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ComicsItem, viewModel: ComicsSearchViewModel) {
            binding.item = item
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        fun update(bundle: Bundle) {
            if (bundle.containsKey(Constants.PAYLOAD_KEY_BOOKMARK)) {
                binding.tbBookmark.isChecked = bundle.getBoolean(Constants.PAYLOAD_KEY_BOOKMARK)
            }
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

    override fun getChangePayload(oldItem: ComicsItem, newItem: ComicsItem): Any? {
        val bundle = Bundle()
        if (oldItem.isBookmarked != newItem.isBookmarked) {
            bundle.putBoolean(Constants.PAYLOAD_KEY_BOOKMARK, newItem.isBookmarked)
        }
        return if (bundle.isEmpty) {
            null
        } else {
            bundle
        }
    }
}