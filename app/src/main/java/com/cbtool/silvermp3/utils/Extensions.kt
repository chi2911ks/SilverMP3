package com.cbtool.silvermp3.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.cbtool.silvermp3.R
import androidx.core.graphics.drawable.toDrawable
import androidx.palette.graphics.Palette
import com.cbtool.silvermp3.data.model.Song

fun Activity.startNewActivity(target: Class<out Activity>, clearStack: Boolean = false) {
    val intent = Intent(this, target)
    if (clearStack) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}

fun Activity.startNewActivity(target: Class<out Activity>, extras: Bundle? = null) {
    val intent = Intent(this, target)
    extras?.let { intent.putExtras(it) }
    startActivity(intent)
}

fun FragmentManager.navigateTo(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false
) {
    val transaction = beginTransaction()
    val current = findFragmentById(containerId)
    val fragmentTag = fragment::class.java.simpleName
    if (current != null && current::class.java == fragment::class.java) {
        Log.d("MainActivity", "Đang ở $fragmentTag, không replace lại.")
        return
    }
    transaction.replace(containerId, fragment, fragmentTag)
    if (addToBackStack) {
        transaction.addToBackStack(fragmentTag)
    }
    transaction.commitAllowingStateLoss()
}

fun Fragment.slideDownAndClose(duration: Long = 400L, onEnd: (() -> Unit)? = null) {
    val view = view ?: return
    view.post {
        val distance = view.height.toFloat()
        val animator = ObjectAnimator.ofFloat(view, "translationY", 0f, distance)
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
        AnimatorSet().apply {
            playTogether(animator, alpha)
            this.duration = duration
            interpolator = android.view.animation.AccelerateDecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                    view.translationY = 0f
                    view.alpha = 1f
                    onEnd?.invoke()
                }
            })
            start()
        }
    }
}

fun Fragment.slideUpAndShow(duration: Long = 400L, onEnd: (() -> Unit)? = null) {
    val view = view ?: return
    view.post {
        view.visibility = View.VISIBLE
//        // do bị transparent nên phải set lại màu, có thể hàm này được gọi trước khi view tạo màu (nghĩ thể)
//        view.background =
//            ContextCompat.getColor(requireContext(), R.color.colorBackground).toDrawable()
        view.alpha = 1f
        view.translationY = view.height.toFloat()
        val animator = ObjectAnimator.ofFloat(view, "translationY", view.translationY, 0f)
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 0.6f, 1f)
        AnimatorSet().apply {
            playTogether(animator, alpha)
            this.duration = duration
            interpolator = android.view.animation.DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    onEnd?.invoke()
                }
            })
            start()
        }
    }
}

fun Context.createNicePaletteBackground(bitmap: Bitmap): GradientDrawable {
    val defaultColor = ContextCompat.getColor(this, android.R.color.darker_gray)
    var vibrant = defaultColor
    var muted = defaultColor
    var darkVibrant = defaultColor

    // Tạo palette đồng bộ (blocking)
    val palette = Palette.from(bitmap).generate()

    // Lấy các màu chính
    vibrant = palette.getVibrantColor(defaultColor)
    muted = palette.getMutedColor(vibrant)
    darkVibrant = palette.getDarkVibrantColor(muted)

    // Pha nhẹ vibrant + muted để có màu dịu
    val startColor = ColorUtils.blendARGB(vibrant, muted, 0.3f)
    val endColor = ColorUtils.blendARGB(darkVibrant, muted, 0.6f)

    // Gradient chéo (từ trên trái xuống dưới phải)
    return GradientDrawable(
        GradientDrawable.Orientation.TL_BR,
        intArrayOf(startColor, endColor)
    ).apply {
        cornerRadius = 24f // Bo góc nhẹ cho đẹp
    }
}