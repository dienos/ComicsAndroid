package com.kstd.android.jth.ui.extension

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.kstd.android.jth.ui.feature.dialog.ProgressDialogFragment
import kotlin.math.min

enum class ImageFilter(val value: String) {
    LARGE("large"),
    MEDIUM("medium"),
    SMALL("small")
}

fun Context.getImageFilterByResolution(): String {
    val dm = resources.displayMetrics
    val minScreenPx = min(dm.widthPixels, dm.heightPixels)

    return when {
        minScreenPx >= 1080 -> ImageFilter.LARGE.value
        minScreenPx >= 720 -> ImageFilter.MEDIUM.value
        else -> ImageFilter.SMALL.value
    }
}

fun AppCompatActivity.showProgress() {
    supportFragmentManager.findFragmentByTag(ProgressDialogFragment.TAG) ?: run {
        ProgressDialogFragment.newInstance()
            .show(supportFragmentManager, ProgressDialogFragment.TAG)
    }
}

fun AppCompatActivity.hideProgress() {
    supportFragmentManager.findFragmentByTag(ProgressDialogFragment.TAG)?.let {
        (it as? ProgressDialogFragment)?.dismiss()
    }
}
