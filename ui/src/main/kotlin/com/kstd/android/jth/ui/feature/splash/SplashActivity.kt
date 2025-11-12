package com.kstd.android.jth.ui.feature.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kstd.android.jth.R
import com.kstd.android.jth.ui.feature.home.ComicsActivity
import com.kstd.android.jth.ui.theme.ComicsAppTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            ComicsAppTheme {
                SplashScreen {
                    startActivity(Intent(this@SplashActivity, ComicsActivity::class.java))
                    finish()
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.book_opening),
        cacheKey = if (isDarkTheme) {
            "book_opening_dark"
        } else {
            "book_opening_light"
        }
    )
    val progress by animateLottieCompositionAsState(composition)

    LaunchedEffect(progress) {
        if (progress == 1.0f) {
            delay(200)
            onAnimationFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            enableMergePaths = true
        )
    }
}
