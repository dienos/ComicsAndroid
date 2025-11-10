package com.kstd.android.jth.ui.extension

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import com.kstd.android.jth.ui.feature.dialog.ProgressDialogFragment
import kotlin.math.min
import kotlin.math.roundToInt

fun Context.getImageFilterByResolution(): String {
    val cfg = resources.configuration
    val dm = resources.displayMetrics

    val swDp = if (cfg.smallestScreenWidthDp != Configuration.SMALLEST_SCREEN_WIDTH_DP_UNDEFINED) {
        cfg.smallestScreenWidthDp
    } else {
        val minPx = min(dm.widthPixels, dm.heightPixels)
        (minPx / dm.density).roundToInt()
    }

    return when {
        swDp >= 360 -> "large"
        else -> "all"
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