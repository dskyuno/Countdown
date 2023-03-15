package com.project.timemanagerment.base.util

import android.content.Context
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView

fun loadingDialogXPopup(context: Context): BasePopupView {
    return XPopup.Builder(context)
        .dismissOnBackPressed(false)
        .isLightNavigationBar(true)
        .isViewMode(true)
        .asLoading("加载中")
        .show()
}