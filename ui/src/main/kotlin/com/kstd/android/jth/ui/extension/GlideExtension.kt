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
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.min

@GlideModule
class ComicsGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val memoryCacheSizeBytes = 1024 * 1024 * 20L
        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes))
        val diskCacheSizeBytes = 1024 * 1024 * 500L
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSizeBytes))
    }
}


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
            // Create a fallback bitmap with a scaled-up error icon
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.LTGRAY)

            val errorDrawable = ResourcesCompat.getDrawable(
                resources,
                android.R.drawable.ic_menu_report_image,
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

private fun extractWidthFromThumbnailUrl(url: String): String? {
    val regex = "type=b(\\\\d+)".toRegex()
    val matchResult = regex.find(url)
    return matchResult?.groupValues?.get(1)
}


internal fun ImageView.executeGlide(
    url: String,
    listener: RequestListener<Drawable>
) {
    val height = extractWidthFromThumbnailUrl(url)?.toInt() ?: 150

    val thumbnailRequest = Glide.with(context)
        .load(url)
        .apply(prepareRequestOptions())

    Glide.with(this.context)
        .load(url)
        .override(100, height)
        .thumbnail(thumbnailRequest)
        .transition(DrawableTransitionOptions.withCrossFade())
        .listener(listener)
        .into(this)
}

internal fun ImageView.prepareRequestOptions(): RequestOptions {
    val progressDrawable = createProgressDrawable(this)
    return RequestOptions()
        .sizeMultiplier(0.1f)
        .placeholder(progressDrawable)
        .error(android.R.drawable.ic_menu_report_image)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
}
