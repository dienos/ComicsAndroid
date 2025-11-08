package com.kstd.android.jth.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comics")
data class ComicsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String
)
