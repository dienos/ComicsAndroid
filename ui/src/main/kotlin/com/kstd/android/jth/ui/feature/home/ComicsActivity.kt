package com.kstd.android.jth.ui.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.kstd.android.jth.R
import com.kstd.android.jth.databinding.ActivityComicsBinding
import com.kstd.android.jth.ui.base.BaseActivity
import com.kstd.android.jth.ui.feature.search.ComicsSearchActivity
import com.kstd.android.jth.ui.feature.viewer.WebtoonViewerActivity
import com.kstd.android.jth.ui.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@AndroidEntryPoint
class ComicsActivity : BaseActivity<ActivityComicsBinding>(R.layout.activity_comics) {

    private val viewModel: ComicsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupNavigation()
        observeLoadingState(viewModel)
        observeToastEvents(viewModel)
        observeUiState()
        observeNavigationEvents()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            viewModel.onTabSelected()
            NavigationUI.onNavDestinationSelected(menuItem, navController)
            true
        }

        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setupWithNavController(navController)
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.isBookmarkDeleteMode.collectLatest {
                invalidateOptionsMenu()
            }
        }
        lifecycleScope.launch {
            viewModel.isHomeSelectionMode.collectLatest {
                invalidateOptionsMenu()
            }
        }
    }

    private fun observeNavigationEvents() {
        lifecycleScope.launch {
            viewModel.navigateToViewerEvent.collectLatest { (selectedIndex, comics) ->
                val intent = Intent(this@ComicsActivity, WebtoonViewerActivity::class.java).apply {
                    putExtra(Constants.EXTRA_SELECTED_INDEX, selectedIndex)
                    putParcelableArrayListExtra(Constants.EXTRA_COMICS_LIST, ArrayList(comics))
                }
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        when {
            viewModel.isBookmarkDeleteMode.value -> {
                menuInflater.inflate(R.menu.bookmark_selection_menu, menu)
                return true
            }
            viewModel.isHomeSelectionMode.value -> {
                menuInflater.inflate(R.menu.home_selection_menu, menu)
                return true
            }
            else -> {
                menuInflater.inflate(R.menu.menu_search, menu)
                return true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                viewModel.onDeleteBookmarksClick()
                true
            }
            R.id.action_add_bookmark -> {
                viewModel.onAddBookmarksClick()
                true
            }
            R.id.action_cancel -> {
                viewModel.cancelSelectionMode()
                true
            }
            R.id.action_search -> {
                val intent = Intent(this, ComicsSearchActivity::class.java).apply {
                    val comicsList = viewModel.currentComicsList
                    putParcelableArrayListExtra(Constants.EXTRA_COMICS_LIST, ArrayList(comicsList))
                }
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
