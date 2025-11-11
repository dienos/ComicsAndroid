package com.kstd.android.jth.ui.feature.bookmark

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kstd.android.jth.R
import com.kstd.android.jth.databinding.FragmentBookmarkBinding
import com.kstd.android.jth.ui.base.BaseFragment
import com.kstd.android.jth.ui.feature.home.ComicsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>(R.layout.fragment_bookmark) {

    private val viewModel: ComicsViewModel by activityViewModels()
    private val bookmarkAdapter by lazy { BookmarkAdapter(viewModel) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBinding()
        setupRecyclerView()
        setupSwipeRefresh()
        observeData()
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupRecyclerView() {
        binding.rvBookmarkFragment.setHasFixedSize(true)
        binding.rvBookmarkFragment.adapter = bookmarkAdapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshBookmark()
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isRefreshing.collectLatest {
                binding.swipeRefreshLayout.isRefreshing = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bookmarksFlow.collectLatest { bookmarks ->
                val isEmpty = bookmarks.isEmpty()
                binding.rvBookmarkFragment.isVisible = !isEmpty
                binding.tvEmptyBookmark.isVisible = isEmpty
            }
        }
    }
}
