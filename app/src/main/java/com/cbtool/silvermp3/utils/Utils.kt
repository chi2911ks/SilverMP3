package com.cbtool.silvermp3.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.cbtool.silvermp3.R

fun loadImagePalette(
    context: Context,
    image: ImageView,
    imageURL: String,
    onResultColor: (Int) -> Unit
) {
    Glide.with(context)
        .asBitmap()
        .load(imageURL)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(
                resource: Bitmap,
                transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
            ) {
                image.setImageBitmap(resource)
                // Dùng Palette để trích màu chủ đạo
                Palette.from(resource).generate { palette ->
                    val defaultColor = ContextCompat.getColor(context, R.color.dark)
                    val vibrant = palette?.getVibrantColor(defaultColor) ?: defaultColor
                    val muted = palette?.getMutedColor(defaultColor) ?: defaultColor
                    val finalColor = ColorUtils.blendARGB(vibrant, muted, 0.4f)
                    onResultColor(finalColor)
                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })
}