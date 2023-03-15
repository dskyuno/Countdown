package com.project.timemanagerment.feature.profile.ui.register

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ScaleXSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.StringUtils
import com.project.timemanagerment.base.util.ToastUtilsDraK
import com.project.timemanagerment.base.util.loadingDialogXPopup
import com.project.timemanagerment.databinding.FragmentRegisterBinding
import com.project.timemanagerment.feature.profile.data.datasource.network.model.requestbean.RegisterAccountBean
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    val viewModel: RegisterViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvPassword.setText(justifyString("密码", 4))
        setToolbar();
        initListener()
        initClickNavigation()
    }

    private fun setToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initListener() {
        binding.tvRegister.setOnClickListener {
            if (handlerRegisterEditText()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    val loadingDialog = loadingDialogXPopup(requireContext())
                    val registerAccountBean =
                        RegisterAccountBean(
                            binding.etAccount.text.toString(),
                            binding.etEmail.text.toString(),
                            binding.etPassword.text.toString(),
                            binding.etPasswordAgain.text.toString()
                        )
                    val response = viewModel.registerAccount(registerAccountBean)
                    when (response.code) {
                        200 -> {
                            ToastUtilsDraK.showMsgCenter(response.data!!)
                        }
                        400 -> {
                            ToastUtilsDraK.showMsgCenter(response.data!!)
                        }
                        else -> {
                            ToastUtilsDraK.showMsgCenter(response.msg + response.code)
                        }
                    }

                }

            }

        }
    }


    private fun isEmpty(s: String): Boolean {
        return StringUtils.isEmpty(s) || s.trim { it <= ' ' }.isEmpty()
    }

    private fun isValidEmailFormat(email: String?): Boolean { //邮箱判断正则表达式
        val pattern = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")
        val mc = pattern.matcher(email)
        return mc.matches()
    }

    private fun handlerRegisterEditText(): Boolean {
        return if (isEmpty(
                binding.etAccount.text.toString()
            )
        ) { // ToastUtil.showMsg(_mActivity, "账号不能为空");
            ToastUtilsDraK.showMsgTop("账号不能为空")
            false
        } else if (isEmpty(binding.etEmail.text.toString())) {
            ToastUtilsDraK.showMsgTop("邮箱不能为空")
            false
        } else if (!isValidEmailFormat(binding.etEmail.text.toString())) {
            ToastUtilsDraK.showMsgTop("邮箱格式不正确")
            false
        } else if (binding.etPassword.text.toString().isEmpty()) {
            ToastUtilsDraK.showMsgTop("密码不能为空")
            false
        } else if (binding.etPasswordAgain.text.toString().isEmpty()) {
            ToastUtilsDraK.showMsgTop("确定密码不能为空")
            false
        } else if (binding.etPassword.text.toString().length < 6 || binding.etPasswordAgain.getText()
                .toString().length < 6
        ) {
            ToastUtilsDraK.showMsgTop("密码不少于6位")
            false
        } else if (binding.etPassword.text.toString() != binding.etPasswordAgain.text.toString()) {
            ToastUtilsDraK.showMsgTop("两次密码不一致")
            false
        } else if (!binding.cbAgreement.isChecked) {
            ToastUtilsDraK.showMsgTop("需同意用户协议和隐私政策")
            hideKeyboard()
            false
        } else {
            true
        }
    }

    fun Fragment.hideKeyboard() = ViewCompat.getWindowInsetsController(requireView())
        ?.hide(WindowInsetsCompat.Type.ime())

    private fun initClickNavigation() {
        binding.tvToLogin.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.tvUserAgreement.setOnClickListener {
            val directions = RegisterFragmentDirections.navToUserAgreementFragment()
            findNavController().navigate(directions)
        }
        binding.tvPrivacyPolicy.setOnClickListener {
            val directions = RegisterFragmentDirections.navToPrivacyPolicyFragment()
            findNavController().navigate(directions)
        }

    }

    /**
     * 将给定的字符串给定的长度两端对齐
     *
     * @param str  待对齐字符串
     * @param size 汉字个数，eg:size=5，则将str在5个汉字的长度里两端对齐
     * @Return
     */
    fun justifyString(str: String, size: Int): SpannableStringBuilder? {
        val spannableStringBuilder = SpannableStringBuilder()
        if (TextUtils.isEmpty(str)) {
            return spannableStringBuilder
        }
        val chars = str.toCharArray()
        if (chars.size >= size || chars.size == 1) {
            return spannableStringBuilder.append(str)
        }
        val l = chars.size
        val scale = (size - l).toFloat() / (l - 1)
        for (i in 0 until l) {
            spannableStringBuilder.append(chars[i])
            if (i != l - 1) {
                val s = SpannableString("　") //全角空格
                s.setSpan(ScaleXSpan(scale), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableStringBuilder.append(s)
            }
        }
        return spannableStringBuilder
    }
}