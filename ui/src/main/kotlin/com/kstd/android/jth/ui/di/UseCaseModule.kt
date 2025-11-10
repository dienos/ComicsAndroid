package com.kstd.android.jth.ui.di

import com.kstd.android.jth.domain.repository.ComicsRepository
import com.kstd.android.jth.domain.usecase.AddBookMarkUseCase
import com.kstd.android.jth.domain.usecase.DeleteBookmarkUseCase
import com.kstd.android.jth.domain.usecase.FetchComicsUseCase
import com.kstd.android.jth.domain.usecase.GetBookMarkUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun providesFetchComicsUseCase(repository: ComicsRepository): FetchComicsUseCase {
        return FetchComicsUseCase(repository)
    }

    @Provides
    fun providesGetBookMarkUseCase(repository: ComicsRepository): GetBookMarkUseCase {
        return GetBookMarkUseCase(repository)
    }

    @Provides
    fun providesAddBookMarkUseCase(repository: ComicsRepository): AddBookMarkUseCase {
        return AddBookMarkUseCase(repository)
    }

    @Provides
    fun providesDeleteBookMarkUseCase(repository: ComicsRepository): DeleteBookmarkUseCase {
        return DeleteBookmarkUseCase(repository)
    }
}