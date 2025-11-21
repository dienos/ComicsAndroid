package com.comics.android.jth.ui.feature.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.comics.android.jth.R
import com.comics.android.jth.databinding.FragmentHomeBinding
import com.comics.android.jth.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: ComicsViewModel by activityViewModels()
    private val comicsAdapter by lazy { ComicsAdapter(viewModel) }
    private lateinit var backPressedCallback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackPressedCallback()
        setupBinding()
        setupRecyclerView()
        setupSwipeRefresh()
        setupGuideTouchListener()
        observeData()
    }

    private fun setupBackPressedCallback() {
        backPressedCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                viewModel.cancelSelectionMode()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupRecyclerView() {
        val isTablet = requireContext().resources.getBoolean(R.bool.is_tablet)

        binding.rvComicsFragment.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(10)
            adapter = comicsAdapter
            (itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false
            layoutManager = if (isTablet) {
                GridLayoutManager(requireContext(), 2)
            } else {
                layoutManager
            }
        }

        comicsAdapter.addLoadStateListener {
            viewModel.updateCurrentComicsList(comicsAdapter.snapshot().items)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.cancelSelectionMode()
            comicsAdapter.refresh()
        }
    }

    private fun setupGuideTouchListener() {
        binding.guideLayout.setOnTouchListener({ _, _ ->
            viewModel.onHomeGuideTouch()
            true
        })
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            comicsAdapter.loadStateFlow.collectLatest { loadStates ->
                val isRefreshing = loadStates.refresh is LoadState.Loading
                binding.swipeRefreshLayout.isRefreshing = isRefreshing
                viewModel.setLoading(isRefreshing)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isHomeSelectionMode.collectLatest { isSelectionMode ->
                backPressedCallback.isEnabled = isSelectionMode
            }
        }
    }
}
