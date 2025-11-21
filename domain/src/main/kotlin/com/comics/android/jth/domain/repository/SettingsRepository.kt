package com.comics.android.jth.domain.repository

interface SettingsRepository {
    fun isHomeGuideShown(key: String): Boolean
    suspend fun setHomeGuideShown(key: String, value: Boolean)

    fun isBookMarkGuideShown(key: String): Boolean
    suspend fun setBookMarkGuideShown(key: String, value: Boolean)
}
