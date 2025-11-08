package com.kstd.android.jth.domain.di

import com.kstd.android.jth.data.repository.SampleRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import com.kstd.android.jth.domain.usecase.GetLocalSampleUseCase
import com.kstd.android.jth.domain.usecase.GetSampleUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun providesGetSampleUseCase(repository: SampleRepositoryImpl): GetSampleUseCase {
        return GetSampleUseCase(repository)
    }

    @Provides
    fun providesGetLocalSampleUseCase(repository: SampleRepositoryImpl): GetLocalSampleUseCase {
        return GetLocalSampleUseCase(repository)
    }
}