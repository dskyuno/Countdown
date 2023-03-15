package com.project.timemanagerment.base.util

import com.project.timemanagerment.R

enum class CountdownThemeList(
    val themeIndex: Int,
    val topColor: Int,
    val centerColor: Int,
    val bottomColor: Int,
    val introduction: String,
    val backgroundColorString: String
) {
    /*前三过去现在未来*/
    theme_0(
        0,
        R.color.countdown_theme_top_0,
        R.color.countdown_theme_center_0,
        R.color.countdown_theme_bottom_0,
        "默认自适应",
        "#ffcc00"
    ),
    theme_1(
        1,
        R.color.countdown_theme_top_1,
        R.color.countdown_theme_center_1,
        R.color.countdown_theme_bottom_1,
        "默认过去",
        "#ffcc00"
    ),
    theme_2(
        2,
        R.color.countdown_theme_top_2,
        R.color.countdown_theme_center_2,
        R.color.countdown_theme_bottom_2,
        "默认现在",
        "#ffcc00"
    ),
    theme_3(
        3,
        R.color.countdown_theme_top_3,
        R.color.countdown_theme_center_3,
        R.color.countdown_theme_bottom_3,
        "默认未来",
        "#ffcc00"
    ),
    theme_4(
        4,
        R.color.countdown_theme_top_4,
        R.color.countdown_theme_center_4,
        R.color.countdown_theme_bottom_4,
        "湛蓝",
        "#ffcc00"
    ),
    theme_5(
        5,
        R.color.countdown_theme_top_5,
        R.color.countdown_theme_center_5,
        R.color.countdown_theme_bottom_5,
        "蓝色",
        "#ffcc00"
    ),
    theme_6(
        6,
        R.color.countdown_theme_top_6,
        R.color.countdown_theme_center_6,
        R.color.countdown_theme_bottom_6,
        "清新",
        "#ffcc00"
    ),
    theme_7(
        7,
        R.color.countdown_theme_top_7,
        R.color.countdown_theme_center_7,
        R.color.countdown_theme_bottom_7,
        "绿色",
        "#ffcc00"
    ),
    theme_8(
        8,
        R.color.countdown_theme_top_8,
        R.color.countdown_theme_center_8,
        R.color.countdown_theme_bottom_8,
        "淡雅",
        "#ffcc00"
    ),
    theme_9(
        9,
        R.color.countdown_theme_top_9,
        R.color.countdown_theme_center_9,
        R.color.countdown_theme_bottom_9,
        "橙黄",
        "#ffcc00"
    ),
    theme_10(
        10,
        R.color.countdown_theme_top_10,
        R.color.countdown_theme_center_10,
        R.color.countdown_theme_bottom_10,
        "淡紫",
        "#ffcc00"
    ),
    theme_11(
        11,
        R.color.countdown_theme_top_11,
        R.color.countdown_theme_center_11,
        R.color.countdown_theme_bottom_11,
        "轻松",
        "#ffcc00"
    ),
    theme_12(
        12,
        R.color.countdown_theme_top_12,
        R.color.countdown_theme_center_12,
        R.color.countdown_theme_bottom_12,
        "淡雅",
        "#ffcc00"
    );


    companion object {

        fun getThemeByIndex(themeIndex: Int): CountdownTheme? {
            for (element in values()) {
                if (element.themeIndex == themeIndex) {
                    return CountdownTheme(
                        element.themeIndex,
                        element.topColor,
                        element.centerColor,
                        element.bottomColor,
                        element.introduction,
                        element.backgroundColorString
                    )
                }
            }
            return null
        }

        fun getThemeList(): MutableList<CountdownTheme> {
            val themeList = mutableListOf<CountdownTheme>()
            for (element in values()) {
                val theme = CountdownTheme(
                    element.themeIndex,
                    element.topColor,
                    element.centerColor,
                    element.bottomColor,
                    element.introduction,
                    element.backgroundColorString
                )
                themeList.add(theme)
            }
            return themeList
        }
    }
}

data class CountdownTheme(
    val themeIndex: Int,
    val topColor: Int,
    val centerColor: Int,
    val bottomColor: Int,
    val introduction: String,
    val backgroundColorString: String
)