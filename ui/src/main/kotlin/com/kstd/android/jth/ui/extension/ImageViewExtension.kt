package com.kstd.android.jth.ui.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun ImageView.bindImageUrl(url: String?) {
    if (url.isNullOrEmpty()) return

    Glide.with(this.context)
        .load(url)
        .into(this)
}