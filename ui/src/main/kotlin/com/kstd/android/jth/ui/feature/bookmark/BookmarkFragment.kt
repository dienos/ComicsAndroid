package com.kstd.android.jth.ui.feature.bookmark

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.kstd.android.jth.R
import com.kstd.android.jth.databinding.FragmentBookmarkBinding
import com.kstd.android.jth.ui.base.BaseFragment
import com.kstd.android.jth.ui.feature.home.ComicsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>(R.layout.fragment_bookmark) {

    private val viewModel: ComicsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getBookmarks()
    }
}