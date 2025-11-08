package com.kstd.android.jth.ui.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.kstd.android.jth.R
import com.kstd.android.jth.databinding.FragmentHomeBinding
import com.kstd.android.jth.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: ComicsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.fetchComics()
    }
}