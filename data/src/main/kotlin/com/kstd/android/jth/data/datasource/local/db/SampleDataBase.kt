package com.kstd.android.jth.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kstd.android.jth.data.datasource.local.entity.SampleEntity

@Database(
    entities = [SampleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SampleDataBase : RoomDatabase() {
   abstract fun SampleDao() : SampleDao
}