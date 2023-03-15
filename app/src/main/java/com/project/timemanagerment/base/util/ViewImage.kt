package com.project.timemanagerment.base.util

import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.view.PixelCopy
import android.view.View
import android.view.Window

class ViewImage {
    companion object {
        @JvmStatic
        fun getScreenShotFromView(view: View, window: Window, callback: (Bitmap) -> Unit) {
            val bitmap: Bitmap
            // PixelCopy is available since API 24 but doesn't seem to work 100% until API 29.
            // The build version statement can be adjusted according to how well PixelCopy
            // works in your environment before "P".
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val locationOfView = IntArray(2)
                view.getLocationInWindow(locationOfView)
                val rect = Rect(
                    locationOfView[0], locationOfView[1],
                    locationOfView[0] + view.width, locationOfView[1] + view.height
                )

                bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                try {
                    PixelCopy.request(
                        window, rect, bitmap,
                        { copyResult: Int ->
                            if (copyResult == PixelCopy.SUCCESS) {
                                callback(bitmap)
                            }
                            // possible to handle other result codes ...
                        },
                        Handler()
                    )
                } catch (e: IllegalArgumentException) {
                    // PixelCopy may throw IllegalArgumentException, make sure to handle it
                    e.printStackTrace()
                }
            } else {
                bitmap = getScreenShot(view)
                callback(bitmap)
            }
        }

        //      Method which will return Bitmap after taking screenshot. We have to pass the view which
//      we want to take screenshot.
        @Suppress("DEPRECATION")
        @JvmStatic
        fun getScreenShot(view: View): Bitmap {
            view.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
            return bitmap
        }
    }
}