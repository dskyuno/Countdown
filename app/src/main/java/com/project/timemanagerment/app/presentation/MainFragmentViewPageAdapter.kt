package com.project.timemanagerment.app.presentation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.timemanagerment.feature.home.ui.home.HomeFragment
import com.project.timemanagerment.feature.profile.ui.profile.ProfileFragment


const val HOME_PAGE_INDEX = 0
const val PROFILE_PAGE_INDEX = 1

class MainFragmentViewPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        HOME_PAGE_INDEX to { HomeFragment() },
        PROFILE_PAGE_INDEX to { ProfileFragment() }
    )

    override fun getItemCount(): Int {
        return tabFragmentsCreators.size
    }

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}