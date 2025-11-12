package com.kstd.android.jth.ui.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.min

/**
 * A suspend extension function to fetch a Bitmap using Glide from a given URL.
 * It gracefully handles failures by returning a generated error bitmap instead of throwing an exception.
 */
suspend fun Context.getBitmapWithGlide(url: String, width: Int, height: Int): Bitmap = withContext(Dispatchers.IO) {
    try {
        Glide.with(this@getBitmapWithGlide)
            .asBitmap()
            .load(url)
            .override(width, height)
            .submit()
            .get()
    } catch (e: Exception) {
        Log.e("GlideExtension", "Failed to load bitmap for url: $url", e)
        // Create a fallback bitmap with a scaled-up error icon
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.LTGRAY)

        val errorDrawable = ResourcesCompat.getDrawable(resources, android.R.drawable.ic_menu_report_image, null)
        errorDrawable?.let {
            val iconSize = min(width, height) / 4
            val left = (width - iconSize) / 2
            val top = (height - iconSize) / 2
            it.setBounds(left, top, left + iconSize, top + iconSize)
            it.draw(canvas)
        }
        bitmap // Return the generated error bitmap
    }
}

// --- Internal functions for use by DataBinding Adapters ---

internal fun createProgressDrawable(imageView: ImageView): CircularProgressDrawable {
    return CircularProgressDrawable(imageView.context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

internal fun ImageView.executeGlide(url: String, requestOptions: RequestOptions, listener: RequestListener<Drawable>) {
    val thumbnailRequest = Glide.with(context)
        .load(url)
        .apply(RequestOptions().sizeMultiplier(0.1f))

    Glide.with(this.context)
        .load(url)
        .apply(requestOptions)
        .thumbnail(thumbnailRequest)
        .transition(DrawableTransitionOptions.withCrossFade())
        .listener(listener)
        .into(this)
}

internal fun ImageView.prepareRequestOptions(
    width: String,
    height: String,
    defaultScaleType: ImageView.ScaleType
): RequestOptions {
    val progressDrawable = createProgressDrawable(this)
    var requestOptions = RequestOptions()
        .placeholder(progressDrawable)
        .error(android.R.drawable.ic_menu_report_image) // Use system default error icon
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

    this.scaleType = defaultScaleType

    if (width.toInt() > 0 && height.toInt() > 0) {
        requestOptions = requestOptions.override(width.toInt(), height.toInt())
    }

    return requestOptions
}
