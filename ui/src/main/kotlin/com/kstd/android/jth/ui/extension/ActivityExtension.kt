package com.kstd.android.jth.ui.extension

import androidx.appcompat.app.AppCompatActivity
import com.kstd.android.jth.ui.feature.dialog.ProgressDialogFragment

fun AppCompatActivity.showProgress() {
    supportFragmentManager.findFragmentByTag(ProgressDialogFragment.TAG) ?: run {
        ProgressDialogFragment.newInstance().show(supportFragmentManager, ProgressDialogFragment.TAG)
    }
}

fun AppCompatActivity.hideProgress() {
    supportFragmentManager.findFragmentByTag(ProgressDialogFragment.TAG)?.let {
        (it as? ProgressDialogFragment)?.dismiss()
    }
}