package com.project.timemanagerment.base.util

import android.graphics.Color
import android.view.Gravity
import com.blankj.utilcode.util.ToastUtils
import com.project.timemanagerment.R


class ToastUtils {
    companion object {
        fun showMsg(text: String) {
            ToastUtils.make().setBgResource(R.drawable.bg_toast_light).setGravity(
                Gravity.BOTTOM, 0, 300
            ).show(text)
        }

        fun showMsgCenter(text: String) {
            ToastUtils.make().setBgResource(R.drawable.bg_toast_light)
                .setGravity(
                    Gravity.CENTER, 0, 0
                ).show(text)
        }

        fun showMsgBottom(text: String) {
            ToastUtils.make().setBgResource(R.drawable.bg_toast_light)
                .setGravity(
                    Gravity.CENTER, 0, 0
                ).show(text)
        }
    }
}

class ToastUtilsDraK {
    companion object {
        fun showMsg(text: String) {
            ToastUtils.make().setBgResource(R.drawable.bg_toast_dark).setGravity(
                Gravity.BOTTOM, 0, 300
            )
                .setTextColor(Color.parseColor("#F9F9F9")).show(text)
        }

        fun showMsgCenter(text: String) {
            ToastUtils.make().setBgResource(R.drawable.bg_toast_dark)
                .setTextColor(Color.parseColor("#F9F9F9")).setGravity(
                    Gravity.CENTER, 0, 0
                ).show(text)
        }

        fun showMsgTop(text: String) {
            ToastUtils.make().setBgResource(R.drawable.bg_toast_dark)
                .setTextColor(Color.parseColor("#F9F9F9")).setGravity(
                    Gravity.TOP, 0, 20
                ).show(text)
        }

        fun showMsgBottom(text: String) {
            ToastUtils.make().setBgResource(R.drawable.bg_toast_dark)
                .setTextColor(Color.parseColor("#F9F9F9")).setGravity(
                    Gravity.CENTER, 0, 0
                ).show(text)
        }
    }
}

