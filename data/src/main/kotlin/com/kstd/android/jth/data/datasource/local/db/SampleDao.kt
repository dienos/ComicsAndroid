package com.kstd.android.jth.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kstd.android.jth.data.datasource.local.entity.SampleEntity

@Dao
interface SampleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: SampleEntity)

    @Query("SELECT * FROM sample")
    suspend fun getSamples(): List<SampleEntity>
}