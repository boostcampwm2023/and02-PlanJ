package com.boostcamp.planj.ui.schedule

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

fun Context.setDialogSize(dialogFragment: DialogFragment, heightRatio: Double? = null) {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val params: ViewGroup.LayoutParams? = dialogFragment.dialog?.window?.attributes

    if (Build.VERSION.SDK_INT < 30) {
        val display = windowManager.defaultDisplay
        val size = Point()

        display.getSize(size)

        val deviceWidth = size.x
        val deviceHeight = size.y
        params?.width = (deviceWidth * 0.9).toInt()
        if (heightRatio != null) {
            params?.height = (deviceHeight * heightRatio).toInt()
        }
    } else {
        val rect = windowManager.currentWindowMetrics.bounds

        params?.width = (rect.width() * 0.9).toInt()
        if (heightRatio != null) {
            params?.height = (rect.height() * heightRatio).toInt()
        }
    }

    dialogFragment.dialog?.window?.attributes = params as WindowManager.LayoutParams
}