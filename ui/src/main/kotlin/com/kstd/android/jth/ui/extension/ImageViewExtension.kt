package com.kstd.android.jth.ui.extension

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kstd.android.jth.R

/**
 * A Data Binding adapter to load an image from a URL into an ImageView.
 * This is used in XML layouts for lists (e.g., home, bookmarks).
 */
@BindingAdapter("imageUrl", "imageWidth", "imageHeight", requireAll = false)
fun ImageView.bindImageUrl(url: String?, width: String?, height: String?) {
    if (url.isNullOrEmpty() || width.isNullOrEmpty() || height.isNullOrEmpty()) {
        setImageResource(R.drawable.ic_launcher_background)
        return
    }

    Glide.with(context).clear(this)

    val options = prepareRequestOptions(
        width,
        height,
        ImageView.ScaleType.CENTER_CROP
    )

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
            if (this@bindImageUrl.width > 0 && this@bindImageUrl.height > 0) {
                val viewRatio = this@bindImageUrl.height.toFloat() / this@bindImageUrl.width.toFloat()
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

    executeGlide(url, options, listener)
}
