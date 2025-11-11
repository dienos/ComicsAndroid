package com.kstd.android.jth.ui.glide

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule

/**
 * A custom Glide module to configure cache sizes and other options for the app.
 * This is the standard way to tune Glide for performance in image-heavy applications.
 */
@GlideModule
class ComicsGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val memoryCacheSizeBytes = 1024 * 1024 * 20L
        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes))
        val diskCacheSizeBytes = 1024 * 1024 * 500L
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSizeBytes))
    }
}
