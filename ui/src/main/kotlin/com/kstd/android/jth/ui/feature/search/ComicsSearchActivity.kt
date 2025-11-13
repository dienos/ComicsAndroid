package com.kstd.android.jth.ui.feature.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.kstd.android.jth.R
import com.kstd.android.jth.databinding.ActivityComicsSearchBinding
import com.kstd.android.jth.domain.model.remote.ComicsItem
import com.kstd.android.jth.ui.base.BaseActivity
import com.kstd.android.jth.ui.extension.getParcelableArrayList
import com.kstd.android.jth.ui.feature.viewer.WebtoonViewerActivity
import com.kstd.android.jth.ui.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ComicsSearchActivity : BaseActivity<ActivityComicsSearchBinding>(R.layout.activity_comics_search) {

    private val viewModel: ComicsSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        getComicsListFromIntent()
        setupToolbar()
        setupRecyclerView()
        observeToastEvents(viewModel)
        observeNavigationEvents()
    }

    private fun getComicsListFromIntent() {
        val comics = intent.getParcelableArrayList<ComicsItem>(Constants.EXTRA_COMICS_LIST) ?: emptyList()
        viewModel.setInitialComics(comics)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.comics_search_title)
    }

    private fun setupRecyclerView() {
        binding.rvSearchComics.setHasFixedSize(true)
        binding.rvSearchComics.adapter = ComicsSearchAdapter(viewModel)
    }

    private fun observeNavigationEvents() {
        lifecycleScope.launch {
            viewModel.navigateToViewerEvent.collectLatest { (selectedIndex, comics) ->
                val intent = Intent(this@ComicsSearchActivity, WebtoonViewerActivity::class.java).apply {
                    putExtra(Constants.EXTRA_SELECTED_INDEX, selectedIndex)
                    putParcelableArrayListExtra(Constants.EXTRA_COMICS_LIST, ArrayList(comics))
                }
                startActivity(intent)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}