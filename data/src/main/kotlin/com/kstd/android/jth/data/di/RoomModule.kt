package com.kstd.android.jth.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.kstd.android.jth.data.datasource.local.db.ComicsDataBase
import com.kstd.android.jth.data.datasource.local.db.ComicsDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {
    private const val NAME = "comics.db"

    @Provides
    @Singleton
    fun provideComicsDao(dataBase: ComicsDataBase): ComicsDao {
        return dataBase.comicsDao()
    }

    @Provides
    @Singleton
    fun provideComicsDataBase(
        @ApplicationContext context: Context
    ): ComicsDataBase = Room
        .databaseBuilder(context, ComicsDataBase::class.java, NAME)
        .build()
}