package com.project.timemanagerment.app.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.project.timemanagerment.R
import com.project.timemanagerment.UserPreferences
import com.project.timemanagerment.base.util.ToastUtilsDraK
import com.project.timemanagerment.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    var lastTime = 0L
    val viewModel: MainFragmentViewModel by viewModels()

    val CONSENT_AGREEMENT_AND_POLICY = booleanPreferencesKey("consent_agreement_and_policy")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        val viewPager2 = binding.viewPager
        viewPager2.isUserInputEnabled = false
        viewPager2.offscreenPageLimit = 1
        viewPager2.adapter = MainFragmentViewPageAdapter(this)
        binding.bottomNav.setOnItemSelectedListener {
            // ToastUtil.showMsg(context, "0")
            when (it.itemId) {
                R.id.homeFragment -> {
                    viewPager2.setCurrentItem(0, false)
                    return@setOnItemSelectedListener true
                }
                R.id.profileFragment -> {
                    viewPager2.setCurrentItem(1, false)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTime > 2000) {
                ToastUtilsDraK.showMsg("再按一次退出程序")
                lastTime = currentTime

            } else {
                requireActivity().finish()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userReLogin()
    }

    private fun userReLogin() {
        viewLifecycleOwner.lifecycleScope.launch {
            val response = viewModel.reLogin()
            when (response.code) {
                200 -> {
                    val netUser = response.data
                    netUser?.let {
                        val userPreferences = UserPreferences.newBuilder()
                            .setAccount(it.account)
                            .setEmail(it.email)
                            .setNickname(it.nickname)
                            .setSex(it.sex)
                            .setExpireTime(it.vipExpireTime.time).build()
                        viewModel.updateUserPreferences(userPreferences)
                    }

                }
                400 -> {

                }
                else -> {

                }
            }

        }

    }

}