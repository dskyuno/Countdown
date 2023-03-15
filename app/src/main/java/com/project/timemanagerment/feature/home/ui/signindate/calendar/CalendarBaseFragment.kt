package com.project.timemanagerment.feature.home.ui.signindate.calendar

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment

open class CalendarBaseFragment : Fragment() {
    private var isMiUi = false

    fun initWindow() {}
    fun getWindow(): Window {
        return requireActivity().window
    }
    /**
     * 设置小米黑色状态栏字体
     */
    @SuppressLint("PrivateApi")
    private fun setMIUIStatusBarDarkMode() {
        if (isMiUi) {
            val clazz: Class<out Window> = getWindow().javaClass
            try {
                val darkModeFlag: Int
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                extraFlagField.invoke(getWindow(), darkModeFlag, darkModeFlag)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /*
     * 静态域，获取系统版本是否基于MIUI
     */

    init {
        try {
            @SuppressLint("PrivateApi") val sysClass = Class.forName("android.os.SystemProperties")
            val getStringMethod = sysClass.getDeclaredMethod("get", String::class.java)
            val version = getStringMethod.invoke(sysClass, "ro.miui.ui.version.name") as String
            isMiUi =
                version.compareTo("V6") >= 0 && Build.VERSION.SDK_INT < 24
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 设置魅族手机状态栏图标颜色风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    open fun setMeiZuDarkMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (Build.VERSION.SDK_INT >= 24) {
            return false
        }
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                value = if (dark) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }

    @SuppressLint("InlinedApi")
    private fun getStatusBarLightMode(): Int {
        var result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isMiUi) {
                result = 1
            } else if (setMeiZuDarkMode(getWindow(), true)) {
                result = 2
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                result = 3
            }
        }
        return result
    }


    @SuppressLint("InlinedApi")
    protected fun setStatusBarDarkMode() {
        val type = getStatusBarLightMode()
        if (type == 1) {
            setMIUIStatusBarDarkMode()
        } else if (type == 2) {
            setMeiZuDarkMode(getWindow(), true)
        } else if (type == 3) {
            getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
    }
}