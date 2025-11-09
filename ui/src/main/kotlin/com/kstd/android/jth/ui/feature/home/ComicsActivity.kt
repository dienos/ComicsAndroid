package com.kstd.android.jth.ui.feature.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kstd.android.jth.R
import com.kstd.android.jth.databinding.ActivityComicsBinding
import com.kstd.android.jth.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComicsActivity : BaseActivity<ActivityComicsBinding>(R.layout.activity_comics) {

    private val viewModel: ComicsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupNavigation()
        observeLoadingState(viewModel)
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)
        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setupWithNavController(navController)
    }
}