package com.kstd.android.jth.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.kstd.android.jth.data.datasource.local.source.SampleLocalSource
import com.kstd.android.jth.data.datasource.local.source.SampleLocalSourceImpl
import com.kstd.android.jth.data.datasource.remote.source.SampleRemoteSource
import com.kstd.android.jth.data.datasource.remote.source.SampleRemoteSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Singleton
    @Binds
    abstract fun bindsSimpleRemoteSource(source: SampleRemoteSourceImpl): SampleRemoteSource

    @Singleton
    @Binds
    abstract fun bindsSimpleLocalSource(source: SampleLocalSourceImpl): SampleLocalSource
}