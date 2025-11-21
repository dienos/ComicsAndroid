package com.comics.android.jth.ui.di

import com.comics.android.jth.domain.repository.ComicsRepository
import com.comics.android.jth.domain.repository.SettingsRepository
import com.comics.android.jth.domain.usecase.AddBookMarkUseCase
import com.comics.android.jth.domain.usecase.DeleteBookmarkUseCase
import com.comics.android.jth.domain.usecase.FetchComicsUseCase
import com.comics.android.jth.domain.usecase.GetBookMarkUseCase
import com.comics.android.jth.domain.usecase.IsBookMarkGuideShownUseCase
import com.comics.android.jth.domain.usecase.IsHomeGuideShownUseCase
import com.comics.android.jth.domain.usecase.SetBookMarkGuideShownUseCase
import com.comics.android.jth.domain.usecase.SetHomeGuideShownUseCase
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

    @Provides
    fun providesSetGuideShownUseCase(repository: SettingsRepository): SetHomeGuideShownUseCase {
        return SetHomeGuideShownUseCase(repository)
    }

    @Provides
    fun providesGetGuideShownUseCase(repository: SettingsRepository): IsHomeGuideShownUseCase {
        return IsHomeGuideShownUseCase(repository)
    }

    @Provides
    fun providesSetBookMarkGuideShownUseCase(repository: SettingsRepository): SetBookMarkGuideShownUseCase {
        return SetBookMarkGuideShownUseCase(repository)
    }

    @Provides
    fun providesIsBookMarkGuideShownUseCase(repository: SettingsRepository): IsBookMarkGuideShownUseCase {
        return IsBookMarkGuideShownUseCase(repository)
    }
}