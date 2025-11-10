package com.kstd.android.jth.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey
    val link: String,
    val title: String,
    val thumbnail: String,
    val sizeHeight: String,
    val sizeWidth: String,
)
