package com.kstd.android.jth.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kstd.android.jth.data.datasource.local.entity.ComicsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ComicsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: ComicsEntity)

    @Query("SELECT * FROM comics")
    fun getComics(): Flow<List<ComicsEntity>>
}