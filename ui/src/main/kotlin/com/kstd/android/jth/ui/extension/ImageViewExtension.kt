package com.kstd.android.jth.ui.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kstd.android.jth.R
import com.kstd.android.jth.domain.model.remote.ComicsItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun preloadWebtoonImages(context: Context, comics: List<ComicsItem>, scope: CoroutineScope) {
    scope.launch(Dispatchers.IO) {
        comics.forEach { item ->
            item.link?.let {
                Glide.with(context)
                    .downloadOnly()
                    .load(it)
                    .submit()
            }
        }
    }
}

private fun createProgressDrawable(imageView: ImageView): CircularProgressDrawable {
    return CircularProgressDrawable(imageView.context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

private fun ImageView.executeGlide(url: String, requestOptions: RequestOptions, listener: RequestListener<Drawable>) {
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

private fun ImageView.executeGlideForWebtoon(url: String, requestOptions: RequestOptions) {
    Glide.with(this.context)
        .asBitmap()
        .load(url)
        .apply(requestOptions)
        .transition(BitmapTransitionOptions.withCrossFade())
        .listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                Log.e("GlideBindingAdapter", "Image load failed for url: $url", e)
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }
        })
        .into(this)
}

private fun ImageView.prepareWebtoonRequestOptions(): RequestOptions {
    this.scaleType = ImageView.ScaleType.CENTER_CROP
    return RequestOptions()
        .error(R.drawable.ic_launcher_foreground)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .format(DecodeFormat.PREFER_RGB_565)
}

private fun ImageView.prepareRequestOptions(
    width: String,
    height: String,
    defaultScaleType: ImageView.ScaleType
): RequestOptions {
    val progressDrawable = createProgressDrawable(this)
    var requestOptions = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.ic_launcher_foreground)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

    this.scaleType = defaultScaleType

    if (width.toInt() > 0 && height.toInt() > 0) {
        requestOptions = requestOptions.override(width.toInt(), height.toInt())
    }

    return requestOptions
}

fun ImageView.loadAsWebtoon(url: String) {
    if (url.isEmpty()) {
        setImageResource(R.drawable.ic_launcher_background)
        return
    }
    val options = this.prepareWebtoonRequestOptions()
    executeGlideForWebtoon(url, options)
}

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
