package com.comics.android.jth.ui.extension

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.comics.android.jth.ui.feature.dialog.ProgressDialogFragment
import com.comics.android.jth.ui.util.Constants.TAG
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
    supportFragmentManager.findFragmentByTag(TAG) ?: run {
        ProgressDialogFragment.newInstance()
            .show(supportFragmentManager, TAG)
    }
}

fun AppCompatActivity.hideProgress() {
    supportFragmentManager.findFragmentByTag(TAG)?.let {
        (it as? ProgressDialogFragment)?.dismiss()
    }
}
