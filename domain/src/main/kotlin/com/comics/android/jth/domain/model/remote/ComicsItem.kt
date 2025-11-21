package com.comics.android.jth.domain.model.remote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ComicsItem(
    val title: String? = null,
    val link: String? = null,
    val thumbnail: String? = null,
    val sizeHeight: String? = null,
    val sizeWidth: String? = null,
    val isBookmarked: Boolean = false,
    val isChecked: Boolean = false,
    val isSelectionMode: Boolean = false
) : Parcelable
