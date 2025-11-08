package com.kstd.android.jth.data.di

import com.kstd.android.jth.data.datasource.local.source.ComicsLocalSource
import com.kstd.android.jth.data.datasource.local.source.ComicsLocalSourceImpl
import com.kstd.android.jth.data.datasource.remote.source.ComicsRemoteSource
import com.kstd.android.jth.data.datasource.remote.source.ComicsRemoteSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Singleton
    @Binds
    abstract fun bindsSimpleRemoteSource(source: ComicsRemoteSourceImpl): ComicsRemoteSource

    @Singleton
    @Binds
    abstract fun bindsSimpleLocalSource(source: ComicsLocalSourceImpl): ComicsLocalSource
}