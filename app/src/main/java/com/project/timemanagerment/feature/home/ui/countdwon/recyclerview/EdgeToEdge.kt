/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.project.timemanagerment.feature.home.ui.countdwon.recyclerview

import android.content.Context
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.project.timemanagerment.R

/**
 * A utility for edge-to-edge display. It provides several features needed to make the app
 * displayed edge-to-edge on Android Q with gestural navigation.
 */
object EdgeToEdge
    : EdgeToEdgeImpl by if (Build.VERSION.SDK_INT >= 21) EdgeToEdgeApi21() else EdgeToEdgeBase()

private interface EdgeToEdgeImpl {
    fun setTabLayoutAndCl(tabLayout: TabLayout,constraintLayout: ConstraintLayout){}

    /**
     * Configures an app bar and a toolbar for edge-to-edge display.
     * @param appBar An [AppBarLayout].
     * @param toolbar A [Toolbar] in the [appBar].
     */
    fun setUpAppBar(appBar: AppBarLayout, toolbar: Toolbar) {}

    /**
     * Configures a scrolling content for edge-to-edge display.
     * @param scrollingContent A scrolling ViewGroup. This is typically a RecyclerView or a
     * ScrollView. It should be as wide as the screen, and should touch the bottom edge of
     * the screen.
     */
    fun setUpScrollingContent(scrollingContent: ViewGroup) {}
}

private class EdgeToEdgeBase : EdgeToEdgeImpl

@RequiresApi(21)
private class EdgeToEdgeApi21 : EdgeToEdgeImpl {
    override fun setTabLayoutAndCl(tabLayout: TabLayout,constraintLayout: ConstraintLayout) {
        ViewCompat.setOnApplyWindowInsetsListener(tabLayout) { _, windowInsets ->
            val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
           val  dp=pxToDp(tabLayout.context,systemBars.top.toFloat())
            tabLayout.updatePadding(top = systemBars.top)
        constraintLayout.updatePadding(top = systemBars.top)
            windowInsets
        }

    }
    fun pxToDp(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    override fun setUpAppBar(appBar: AppBarLayout, toolbar: Toolbar) {
        val toolbarPadding = toolbar.resources.getDimensionPixelSize(R.dimen.spacing_medium)
        ViewCompat.setOnApplyWindowInsetsListener(appBar) { _, windowInsets ->
            val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            appBar.updatePadding(top = systemBars.top)
            toolbar.updatePadding(
                left = toolbarPadding + systemBars.left,
                right = systemBars.right
            )
            windowInsets
        }
    }

    override fun setUpScrollingContent(scrollingContent: ViewGroup) {
        val originalPaddingLeft = scrollingContent.paddingLeft
        val originalPaddingRight = scrollingContent.paddingRight
        val originalPaddingBottom = scrollingContent.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(scrollingContent) { _, windowInsets ->
            val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            scrollingContent.updatePadding(
                left = originalPaddingLeft + systemBars.left,
                right = originalPaddingRight + systemBars.right,
                bottom = originalPaddingBottom + systemBars.bottom
            )
            windowInsets
        }
    }
}
