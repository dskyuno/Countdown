package com.project.timemanagerment.feature.profile.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.StringUtils
import com.lxj.xpopup.XPopup
import com.project.timemanagerment.base.util.ToastUtilsDraK
import com.project.timemanagerment.base.util.loadingDialogXPopup
import com.project.timemanagerment.databinding.FragmentLoginBinding
import com.project.timemanagerment.feature.profile.data.datasource.network.model.requestbean.LoginBean
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    val viewModel: LoginViewModel by viewModels()
    lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        initListener()
        initClickWithNavigation()
        subscribeUi()
    }

    private fun initClickWithNavigation() {

        binding.tvToRegister.setOnClickListener {
            val directions = LoginFragmentDirections.navToRegisterFragment()
            findNavController().navigate(directions)
        }
        binding.tvForgetPassword.setOnClickListener {
            val directions = LoginFragmentDirections.navToForgetPasswordFragment()
            findNavController().navigate(directions)
        }

        binding.tvUserAgreement.setOnClickListener {
            val directions = LoginFragmentDirections.navToUserAgreementFragment()
            findNavController().navigate(directions)
        }
        binding.tvPrivacyPolicy.setOnClickListener {
            val directions = LoginFragmentDirections.navToPrivacyPolicyFragment()
            findNavController().navigate(directions)
        }
    }

    private fun subscribeUi() {

    }

    @SuppressLint("ServiceCast")
    private fun initListener() {
        binding.tvLogin.setOnClickListener {


            viewLifecycleOwner.lifecycleScope.launch {
                if (handlerAccountAndPassWord()) {
                    //loadingDialog
                    /*val loadDialog = XPopup.Builder(context)
                        .dismissOnBackPressed(false)
                        .isLightNavigationBar(true)
                        .isViewMode(true)
                        .asLoading("加载中")
                        .show()*/
                    val loadDialog = loadingDialogXPopup(requireContext())
                    val bean = LoginBean("166769669", "166769669", "com.project.timemanagerment")
                    val response = viewModel.login(bean)
                    when (response.code) {
                        200 -> {
                            //通过token返回的值
                            val userInfoResponse = viewModel.getUserInfo()
                            when (userInfoResponse.code) {
                                200 -> {
                                    ToastUtilsDraK.showMsg("登录成功")
                                    loadDialog.dismiss()
                                }
                                400 -> {
                                    ToastUtilsDraK.showMsg(userInfoResponse.msg)
                                    loadDialog.dismiss()

                                }
                                else -> {
                                    ToastUtilsDraK.showMsg(userInfoResponse.msg + userInfoResponse.code)
                                    loadDialog.dismiss()
                                }

                            }

                        }
                        400 -> {
                            ToastUtilsDraK.showMsg(response.msg)
                            loadDialog.dismiss()
                        }
                        else -> {
                            ToastUtilsDraK.showMsg(response.msg + response.code)
                            loadDialog.dismiss()
                        }
                    }

                }
            }
        }
    }

    private fun isEmpty(s: String): Boolean {
        return StringUtils.isEmpty(s) || s.trim { it <= ' ' }.isEmpty()
    }

    private fun handlerAccountAndPassWord(): Boolean {
        var checkStringLegal = false
        if (isEmpty(binding.etUserAccountOrEmail.text.toString()) && binding.etPassword.text.isEmpty()) {
            ToastUtilsDraK.showMsgTop("账号和密码不能为空")
        } else if (isEmpty(binding.etUserAccountOrEmail.text.toString()) || binding.etPassword.text.isEmpty()) {
            ToastUtilsDraK.showMsgTop("账号或者密码不能为空")
        } else {
            checkStringLegal = true
        }
        return checkStringLegal
    }


    private fun setToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

    }
}