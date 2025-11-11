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
        binding.rvComicsFragment.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(10)
            adapter = comicsAdapter
        }

        comicsAdapter.addLoadStateListener {
            viewModel.updateCurrentComicsList(comicsAdapter.snapshot().items)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.cancelSelectionMode() // 1. 모든 선택 모드를 먼저 취소합니다.
            comicsAdapter.refresh()         // 2. 그 다음 데이터를 새로고침합니다.
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            comicsAdapter.loadStateFlow.collectLatest { loadStates ->
                val isRefreshing = loadStates.refresh is LoadState.Loading
                binding.swipeRefreshLayout.isRefreshing = isRefreshing
                viewModel.setLoading(isRefreshing)
            }
        }
    }
}
