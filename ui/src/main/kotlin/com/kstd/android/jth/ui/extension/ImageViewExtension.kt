package com.kstd.android.jth.ui.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kstd.android.jth.R

@BindingAdapter("imageUrl", "imageWidth", "imageHeight", requireAll = false)
fun ImageView.bindImageUrl(url: String?, width: String?, height: String?) {
    if (url.isNullOrEmpty()) {
        setImageResource(R.drawable.ic_launcher_background)
        return
    }

    val requestOptions = RequestOptions()
        .placeholder(R.drawable.ic_launcher_background)
        .error(R.drawable.ic_launcher_foreground)
        .diskCacheStrategy(DiskCacheStrategy.ALL)

    width?.toIntOrNull()?.let { widthInt ->
        height?.toIntOrNull()?.let { heightInt ->
            if (widthInt > 0 && heightInt > 0) {
                requestOptions.override(widthInt, heightInt)

                val viewRatio = this.height.toFloat() / this.width.toFloat()
                val imageRatio = heightInt.toFloat() / widthInt.toFloat()

                this.scaleType = if (viewRatio > imageRatio) {
                    ImageView.ScaleType.FIT_CENTER
                } else {
                    ImageView.ScaleType.CENTER_CROP
                }
            } else {
                this.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }
    } ?: run {
        this.scaleType = ImageView.ScaleType.CENTER_CROP
    }

    Glide.with(this.context)
        .load(url)
        .apply(requestOptions)
        .thumbnail(0.1f)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}
