package com.kstd.android.jth

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ComicsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
