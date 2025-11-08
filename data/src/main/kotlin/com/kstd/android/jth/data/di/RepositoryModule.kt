package com.kstd.android.jth.data.di

import com.kstd.android.jth.data.repository.FetchComicsRepositoryImpl
import com.kstd.android.jth.domain.repository.ComicsRepository
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
        comicsRepositoryImpl: FetchComicsRepositoryImpl
    ): ComicsRepository
}
