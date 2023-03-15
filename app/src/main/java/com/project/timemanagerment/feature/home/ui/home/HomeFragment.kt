package com.project.timemanagerment.feature.home.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.LogUtils
import com.project.timemanagerment.app.presentation.HOME_PAGE_INDEX
import com.project.timemanagerment.app.presentation.PROFILE_PAGE_INDEX
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.project.timemanagerment.R
import com.project.timemanagerment.app.dataStore
import com.project.timemanagerment.base.constant.ConstantsPreferencesKey
import com.project.timemanagerment.base.di.AppInfo

import com.project.timemanagerment.databinding.FragmentHomeBinding
import com.project.timemanagerment.feature.home.ui.home.recyclerView.UserAdapter
import com.project.timemanagerment.feature.home.ui.countdwon.CountdownFragment
import com.project.timemanagerment.feature.home.ui.signinwork.SignInWorkFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

const val COUNT_DOWN_PAGE_INDEX = 0
const val CALENDAR_PAGE_INDEX = 1

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeFragmentViewModel by viewModels()

    @Inject
    lateinit var appInfo: AppInfo



    @Inject
    lateinit var userAdapter: UserAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        val pager = binding.pager
         pager.offscreenPageLimit = 1
        pager.adapter = TabFragmentAdapter(this)
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            // tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)

        }.attach()
        binding.ivArrangementMode.setOnClickListener {
            val myFragment = childFragmentManager.findFragmentByTag("f" + 0)
            // val myFragment = childFragmentManager.fragments[pager.currentItem]
            // val myFragment = childFragmentManager.fragments
            // .find { it is CountdownFragment && it.isResumed } as CountdownFragment
            //  (myFragment as CountdownFragment).arrangementModeChange()
            viewLifecycleOwner.lifecycleScope.launch {
                requireContext().dataStore.edit { settings ->
                    val currentMode = settings[ConstantsPreferencesKey.countdownArrangementMode]
                        ?: ConstantsPreferencesKey.countdownArrangementMode_mode_list
                    //LogUtils.e(currentMode.toString() + "oooooooooooooooooooooooooo")
                    settings[ConstantsPreferencesKey.countdownArrangementMode] =
                        if (currentMode == ConstantsPreferencesKey.countdownArrangementMode_mode_list) ConstantsPreferencesKey.countdownArrangementMode_mode_grild else ConstantsPreferencesKey.countdownArrangementMode_mode_list
                }
            }
        }
        //EdgeToEdge.setTabLayoutAndCl(binding.tabLayout,binding.clIcons)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    if (tab.position == COUNT_DOWN_PAGE_INDEX) {
                        binding.ivArrangementMode.visibility = View.VISIBLE
                    } else {
                        binding.ivArrangementMode.visibility = View.GONE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun getTabIcon(position: Int): Int {
        return when (position) {
            COUNT_DOWN_PAGE_INDEX -> R.drawable.tab_count_dowm_selector
            CALENDAR_PAGE_INDEX -> R.drawable.tab_canlendar_selector
            else -> throw IndexOutOfBoundsException()
        }
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            COUNT_DOWN_PAGE_INDEX -> "计时"
            CALENDAR_PAGE_INDEX -> "签到"
            else -> null
        }
    }
}

class TabFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment.requireActivity()) {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        HOME_PAGE_INDEX to { CountdownFragment() },
        PROFILE_PAGE_INDEX to { SignInWorkFragment() }
    )

    override fun getItemCount(): Int {
        return tabFragmentsCreators.size
    }

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException("---=")
    }


}

