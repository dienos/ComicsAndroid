package com.comics.android.jth.data.di

import android.content.Context
import androidx.room.Room
import com.comics.android.jth.data.datasource.local.db.BookmarkDao
import com.comics.android.jth.data.datasource.local.db.BookmarkDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {
    private const val NAME = "bookmark.db"

    @Provides
    @Singleton
    fun provideBookmarkDao(dataBase: BookmarkDataBase): BookmarkDao {
        return dataBase.bookmarkDao()
    }

    @Provides
    @Singleton
    fun provideBookmarkDataBase(
        @ApplicationContext context: Context
    ): BookmarkDataBase = Room
        .databaseBuilder(context, BookmarkDataBase::class.java, NAME)
        .build()
}
