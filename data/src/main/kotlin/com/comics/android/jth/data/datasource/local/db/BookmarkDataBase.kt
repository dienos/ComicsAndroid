package com.comics.android.jth.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.comics.android.jth.data.datasource.local.entity.BookmarkEntity

@Database(entities = [BookmarkEntity::class], version = 1, exportSchema = false)
abstract class BookmarkDataBase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}
