package com.kstd.android.jth.ui.feature.home

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kstd.android.jth.R
import com.kstd.android.jth.databinding.ActivityComicsBinding
import com.kstd.android.jth.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComicsActivity : BaseActivity<ActivityComicsBinding>(R.layout.activity_comics) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)
        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setupWithNavController(navController)
    }
}