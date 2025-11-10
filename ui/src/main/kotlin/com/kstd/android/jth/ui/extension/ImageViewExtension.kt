package com.kstd.android.jth.ui.extension

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kstd.android.jth.R

private fun createProgressDrawable(imageView: ImageView): CircularProgressDrawable {
    return CircularProgressDrawable(imageView.context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

private fun ImageView.executeGlide(url: String, requestOptions: RequestOptions) {
    val thumbnailRequest = Glide.with(context)
        .load(url)
        .apply(RequestOptions().sizeMultiplier(0.1f))

    Glide.with(this.context)
        .load(url)
        .apply(requestOptions)
        .thumbnail(thumbnailRequest)
        .transition(DrawableTransitionOptions.withCrossFade())
        .listener(object : RequestListener<Drawable> {
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
                return false
            }
        })
        .into(this)
}

private fun ImageView.executeGlideForWebtoon(url: String, requestOptions: RequestOptions) {
    Glide.with(this.context)
        .load(url)
        .apply(requestOptions)
        .transition(DrawableTransitionOptions.withCrossFade())
        .listener(object : RequestListener<Drawable> {
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
                return false
            }
        })
        .into(this)
}

private fun ImageView.prepareWebtoonRequestOptions(): RequestOptions {
    val progressDrawable = createProgressDrawable(this)
    this.scaleType = ImageView.ScaleType.CENTER_CROP
    return RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.ic_launcher_foreground)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
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

    if (width.toInt() > 0 && height.toInt() > 0) {
        requestOptions = requestOptions.override(width.toInt(), height.toInt())

        doOnPreDraw { view ->
            if (view.width > 0 && view.height > 0) {
                val viewRatio = view.height.toFloat() / view.width.toFloat()
                val imageRatio = height.toFloat() / width.toFloat()

                scaleType = if (viewRatio > imageRatio) {
                    ImageView.ScaleType.FIT_CENTER
                } else {
                    ImageView.ScaleType.CENTER_CROP
                }
            } else {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }
    } else {
        this.scaleType = defaultScaleType
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
    if(url == null || width == null || height == null) {
        return
    }

    if (url.isEmpty()) {
        setImageResource(R.drawable.ic_launcher_background)
        return
    }

    Glide.with(context).clear(this)

    val options = prepareRequestOptions(
        width,
        height,
        ImageView.ScaleType.CENTER_CROP
    )
    executeGlide(url, options)
}
