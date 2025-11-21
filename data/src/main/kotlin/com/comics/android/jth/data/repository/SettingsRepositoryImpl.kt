package com.comics.android.jth.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.comics.android.jth.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {

    override fun isHomeGuideShown(key: String): Boolean =
        sharedPreferences.getBoolean(key, false)

    override suspend fun setHomeGuideShown(key: String, value: Boolean) =
        withContext(Dispatchers.IO) {
            sharedPreferences.edit { putBoolean(key, value) }
        }

    override fun isBookMarkGuideShown(key: String): Boolean =
        sharedPreferences.getBoolean(key, false)

    override suspend fun setBookMarkGuideShown(key: String, value: Boolean) {
        sharedPreferences.edit { putBoolean(key, value) }
    }
}
