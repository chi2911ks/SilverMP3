package com.cbtool.silvermp3.utils

import android.annotation.SuppressLint

object TimeHelper {
    @SuppressLint("DefaultLocale")
    fun formatDuration(seconds: Long): String {
        if (seconds < 0) return "00:00"
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

}