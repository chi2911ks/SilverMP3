package com.cbtool.silvermp3.ui.custom

//noinspection SuspiciousImport
import android.R
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.ProgressBar

class LoadingDialog(private val context: Context) {

    private var dialog: Dialog? = null

    fun show() {
        if (dialog == null) {
            dialog = Dialog(context).apply {
                val progressBar = ProgressBar(context)
                progressBar.isIndeterminate = true
                progressBar.indeterminateTintList  = ColorStateList.valueOf(Color.WHITE)
                setContentView(progressBar)     // chỉ có vòng xoay
                setCancelable(false)                     // không cho tắt khi bấm ngoài
                window?.setBackgroundDrawableResource(R.color.transparent) // nền trong suốt
            }
        }
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }

    val isShowing: Boolean
        get() = dialog?.isShowing == true
}