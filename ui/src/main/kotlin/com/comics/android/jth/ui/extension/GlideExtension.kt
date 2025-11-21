package com.comics.android.jth.ui.extension

import android.R
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

suspend fun Context.getBitmapWithGlide(url: String, width: Int, height: Int): Bitmap =
    withContext(Dispatchers.IO) {
        try {
            Glide.with(this@getBitmapWithGlide)
                .asBitmap()
                .load(url)
                .override(width, height)
                .submit()
                .get()
        } catch (e: Exception) {
            Log.e("GlideExtension", "Failed to load bitmap for url: $url", e)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.LTGRAY)

            val errorDrawable = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_menu_report_image,
                null
            )
            errorDrawable?.let {
                val iconSize = min(width, height) / 4
                val left = (width - iconSize) / 2
                val top = (height - iconSize) / 2
                it.setBounds(left, top, left + iconSize, top + iconSize)
                it.draw(canvas)
            }
            bitmap
        }
    }

internal fun createProgressDrawable(imageView: ImageView): CircularProgressDrawable {
    return CircularProgressDrawable(imageView.context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

internal fun ImageView.executeGlide(url: String, listener: RequestListener<Drawable>) {
    val thumbnailRequest = Glide.with(context)
        .load(url)
        .apply(prepareRequestOptions())

    Glide.with(this.context)
        .load(url)
        .override(100, 150)
        .thumbnail(thumbnailRequest)
        .transition(DrawableTransitionOptions.withCrossFade())
        .listener(listener)
        .into(this)
}

internal fun ImageView.prepareRequestOptions(): RequestOptions {
    val progressDrawable = createProgressDrawable(this)
    return RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.ic_menu_report_image)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
}