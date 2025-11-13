package com.kstd.android.jth.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.kstd.android.jth.ui.extension.hideProgress
import com.kstd.android.jth.ui.extension.showProgress
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseActivity<T : ViewDataBinding>(@param:LayoutRes private val layoutResId: Int) :
    AppCompatActivity() {
    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId)
    }

    protected fun observeLoadingState(viewModel: BaseViewModel) {
        lifecycleScope.launch {
            viewModel.isLoading.collectLatest {
                if (it) {
                    showProgress()
                } else {
                    hideProgress()
                }
            }
        }
    }

    protected fun observeToastEvents(viewModel: BaseViewModel) {
        lifecycleScope.launch {
            viewModel.toastEvent.collectLatest { message ->
                Toast.makeText(this@BaseActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
