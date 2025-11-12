package com.kstd.android.jth.ui.feature.bookmark

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
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
        setupGuideTouchListener()
        setupSwipeRefresh()
        observeData()
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupRecyclerView() {
        val isTablet = requireContext().resources.getBoolean(R.bool.is_tablet)

        binding.rvBookmarkFragment.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(10)
            adapter = bookmarkAdapter
            layoutManager = if (isTablet) {
                GridLayoutManager(requireContext(), 2)
            } else {
                layoutManager
            }
        }
    }

    private fun setupGuideTouchListener() {
        binding.guideLayout.setOnTouchListener({ _, _ ->
            viewModel.onBookMarkGuideTouch()
            true
        })
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshBookmark()
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isRefreshing.collectLatest { isRefreshing ->
                binding.swipeRefreshLayout.isRefreshing = isRefreshing
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bookmarksFlow.collectLatest { bookmarks ->
                val isEmpty = bookmarks.isEmpty()
                binding.rvBookmarkFragment.isVisible = !isEmpty
                binding.tvEmptyBookmark.isVisible = isEmpty
                bookmarkAdapter.submitList(bookmarks)
            }
        }
    }
}