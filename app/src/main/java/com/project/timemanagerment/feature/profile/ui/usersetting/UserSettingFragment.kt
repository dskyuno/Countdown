package com.project.timemanagerment.feature.profile.ui.usersetting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lxj.xpopup.XPopup
import com.project.timemanagerment.databinding.FragmentUserSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserSettingFragment : Fragment() {
    lateinit var binding: FragmentUserSettingBinding
    val viewModel: UserSettingViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickNavigation()
        initListener()
    }

    private fun initListener() {
        binding.clUserSex.setOnClickListener {
            XPopup.Builder(context)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asCenterList(
                    "性别", arrayOf("男", "女", "保密"),
                    null, 2
                ) { position, text -> }
                .show()

        }
    }

    private fun initClickNavigation() {
        binding.clChangeNickName.setOnClickListener {
            val directions = UserSettingFragmentDirections.navToChangeNickNameFragment()
            findNavController().navigate(directions)
        }
        binding.clChangePassword.setOnClickListener {
            val directions = UserSettingFragmentDirections.navToChangePasswordFragment()
            findNavController().navigate(directions)
        }
        binding.clDeleteAccount.setOnClickListener {

        }
    }
}