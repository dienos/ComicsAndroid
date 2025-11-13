package com.kstd.android.jth.ui.extension

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kstd.android.jth.R

@BindingAdapter("imageUrl", "imageWidth", "imageHeight", requireAll = false)
fun ImageView.bindImageUrl(url: String?, width: String?, height: String?) {
    if (url.isNullOrEmpty() || width.isNullOrEmpty() || height.isNullOrEmpty()) {
        setImageResource(R.drawable.ic_launcher_background)
        return
    }

    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            Log.e("GlideBindingAdapter", "Image load failed for url: $url", e)
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            if (width.toInt() > 0 && height.toInt() > 0) {
                val viewRatio =
                    this@bindImageUrl.height.toFloat() / this@bindImageUrl.width.toFloat()
                val imageRatio = height.toFloat() / width.toFloat()

                this@bindImageUrl.scaleType = if (viewRatio > imageRatio) {
                    ImageView.ScaleType.FIT_CENTER
                } else {
                    ImageView.ScaleType.CENTER_CROP
                }
            }
            return false
        }
    }

    executeGlide(url, listener)
}
