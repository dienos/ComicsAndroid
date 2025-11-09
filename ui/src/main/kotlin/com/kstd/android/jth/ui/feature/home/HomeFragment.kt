package com.kstd.android.jth.ui.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.kstd.android.jth.R
import com.kstd.android.jth.databinding.FragmentHomeBinding
import com.kstd.android.jth.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: ComicsViewModel by activityViewModels()
    private val comicsAdapter by lazy { ComicsAdapter(viewModel) }

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
        binding.rvComicsFragment.adapter = comicsAdapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isRefreshing.collectLatest {
                binding.swipeRefreshLayout.isRefreshing = it
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.refreshEvent.collectLatest {
                comicsAdapter.refresh()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            comicsAdapter.loadStateFlow.collectLatest {
                viewModel.setLoading(it.refresh is LoadState.Loading)
            }
        }
    }
}