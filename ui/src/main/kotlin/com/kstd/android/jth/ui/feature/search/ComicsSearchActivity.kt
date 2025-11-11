package com.kstd.android.jth.ui.feature.search

import android.os.Bundle
import com.kstd.android.jth.R
import com.kstd.android.jth.databinding.ActivityComicsSearchBinding
import com.kstd.android.jth.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComicsSearchActivity : BaseActivity<ActivityComicsSearchBinding>(R.layout.activity_comics_search) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "코믹스 검색"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}