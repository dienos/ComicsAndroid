package com.kstd.android.jth.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kstd.android.jth.data.datasource.local.entity.ComicsEntity

@Database(
    entities = [ComicsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ComicsDataBase : RoomDatabase() {
   abstract fun comicsDao() : ComicsDao
}