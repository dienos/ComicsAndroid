package com.kstd.android.jth.data.di

import com.kstd.android.jth.data.repository.ComicsRepositoryImpl
import com.kstd.android.jth.data.repository.SettingsRepositoryImpl
import com.kstd.android.jth.domain.repository.ComicsRepository
import com.kstd.android.jth.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindComicsRepository(
        comicsRepositoryImpl: ComicsRepositoryImpl
    ): ComicsRepository

    @Binds
    @Singleton
    abstract fun bindSettingRepository(
        comicsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository
}
