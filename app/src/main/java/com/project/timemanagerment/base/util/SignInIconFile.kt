package com.project.timemanagerment.base.util

import androidx.annotation.DrawableRes
import com.project.timemanagerment.R

enum class SignInIconList(val iconIndex: Int,@DrawableRes val iconId: Int) {
    icon_1(1, R.drawable.ic_baseline_work_icon_1),
    icon_2(2, R.drawable.ic_baseline_work_icon_2),
    icon_3(3, R.drawable.ic_baseline_work_icon_3),
    icon_4(4, R.drawable.ic_baseline_work_icon_4),
    icon_5(5, R.drawable.ic_baseline_work_icon_5),
    icon_6(6, R.drawable.ic_baseline_work_icon_6),
    icon_7(7, R.drawable.ic_baseline_work_icon_7),
    icon_8(8, R.drawable.ic_baseline_work_icon_8),
    icon_9(9, R.drawable.ic_baseline_work_icon_9),
    icon_10(10, R.drawable.ic_baseline_work_icon_10),
    icon_11(11, R.drawable.ic_baseline_work_icon_11),
    icon_12(12, R.drawable.ic_baseline_work_icon_12),
    icon_13(13, R.drawable.ic_baseline_work_icon_13),
    icon_14(14, R.drawable.ic_baseline_work_icon_14),
    icon_15(15, R.drawable.ic_baseline_work_icon_15),
    icon_16(16, R.drawable.ic_baseline_work_icon_16),
    icon_17(17, R.drawable.ic_baseline_work_icon_17),
    icon_18(18, R.drawable.ic_baseline_work_icon_18),
    icon_19(19, R.drawable.ic_baseline_work_icon_19),
    icon_20(20, R.drawable.ic_baseline_work_icon_20),
    icon_21(21, R.drawable.ic_baseline_work_icon_21),
    icon_22(22, R.drawable.ic_baseline_work_icon_22);



    companion object {

        fun getAllSignInIcon(): MutableList<SignInIcon> {
            val icons = mutableListOf<SignInIcon>()
            for (element in values()) {
                icons.add(SignInIcon(element.iconIndex, element.iconId))
            }
            return icons
        }

        fun getSignInIconByIndex(index: Int): SignInIcon {
            for (element in values()) {
                if (element.iconIndex == index) {
                    return SignInIcon(element.iconIndex, element.iconId)
                }
            }
            return SignInIcon(SignInIconList.icon_1.iconIndex, SignInIconList.icon_1.iconId)
        }
    }
}


class SignInIcon(val iconIndex: Int,@DrawableRes val iconId: Int)