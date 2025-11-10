package com.kstd.android.jth.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kstd.android.jth.data.datasource.local.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bookmarks: List<BookmarkEntity>)

    @Delete
    fun delete(bookmarks: List<BookmarkEntity>)

    @Query("SELECT * FROM bookmarks")
    fun getBookmarks(): Flow<List<BookmarkEntity>>
}
