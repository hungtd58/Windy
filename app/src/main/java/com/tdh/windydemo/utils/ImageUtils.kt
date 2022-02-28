package com.tdh.windydemo.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget


class ImageUtils {

    companion object {

        /**
         * Displays an image from a URL in an ImageView.
         */
        fun displayImageFromUrl(
            context: Context, url: String?,
            imageView: ImageView
        ) {
            val myOptions: RequestOptions = RequestOptions()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            Glide.with(context)
                .load(url)
                .apply(myOptions)
                .into(imageView)
        }

        fun displayRoundImageFromUrlWithoutCache(
            context: Context, url: String?,
            imageView: ImageView
        ) {
            val myOptions: RequestOptions = RequestOptions()
                .centerCrop()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
            Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(myOptions)
                .into(object : BitmapImageViewTarget(imageView) {
                    override fun setResource(resource: Bitmap?) {
                        val circularBitmapDrawable: RoundedBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource)
                        circularBitmapDrawable.isCircular = true
                        imageView.setImageDrawable(circularBitmapDrawable)
                    }
                })
        }

        /**
         * Displays an image from a URL in an ImageView.
         * If the image is loading or nonexistent, displays the specified placeholder image instead.
         */
        fun displayImageFromUrlWithPlaceHolder(
            context: Context, url: String?,
            imageView: ImageView,
            placeholderResId: Int
        ) {
            val myOptions: RequestOptions = RequestOptions()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(placeholderResId)
            Glide.with(context)
                .load(url)
                .apply(myOptions)
                .into(imageView)
        }

        /**
         * Displays an image from a URL in an ImageView.
         */
        fun displayGifImageFromUrl(
            context: Context,
            url: String?,
            imageView: ImageView
        ) {
            val myOptions: RequestOptions = RequestOptions()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            Glide.with(context)
                .asGif()
                .load(url)
                .apply(myOptions)
                .into(imageView)
        }

        /**
         * Displays an GIF image from a URL in an ImageView.
         */
        fun displayGifImageFromUrl(
            context: Context,
            url: String?,
            imageView: ImageView,
            thumbnailUrl: String?
        ) {
            val myOptions: RequestOptions = RequestOptions()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            if (thumbnailUrl != null) {
                Glide.with(context)
                    .asGif()
                    .load(url)
                    .apply(myOptions)
                    .thumbnail(Glide.with(context).asGif().load(thumbnailUrl))
                    .into(imageView)
            } else {
                Glide.with(context)
                    .asGif()
                    .load(url)
                    .apply(myOptions)
                    .into(imageView)
            }
        }
    }
}