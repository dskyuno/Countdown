package com.project.timemanagerment.feature.profile.ui.changepassword

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ScaleXSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.project.timemanagerment.base.util.ToastUtilsDraK
import com.project.timemanagerment.databinding.FragmentChangePasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {
    val viewModel: ChangePasswordViewModel by viewModels()
    lateinit var binding: FragmentChangePasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvNewPassword.setText(justifyString("新密码", 5))
        setToolbar()
        initListener()
    }

    private fun initListener() {
        binding.tvChangePasswordSubmit.setOnClickListener {
            if (handlerUserInput()) {

            }
        }
    }

    private fun setToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun handlerUserInput(): Boolean {
        return if (binding.etOldPassword.text.toString().isEmpty()) {
            ToastUtilsDraK.showMsgTop("原密码不能为空")
            false
        } else if (binding.etNewPassword.text.toString().isEmpty()) {
            ToastUtilsDraK.showMsgTop("新密码不能为空")
            false
        } else if (binding.etOldPassword.text.toString().length < 6 || binding.etNewPasswordAgain.getText()
                .toString().length < 6
        ) {
            ToastUtilsDraK.showMsgTop("密码不少于6位")
            false
        } else if (binding.etNewPassword.text.toString() != binding.etNewPasswordAgain.text.toString()) {
            ToastUtilsDraK.showMsgTop("两次密码不一致")
            false
        } else {
            true
        }
    }

    /**
     * 将给定的字符串给定的长度两端对齐
     *
     * @param str  待对齐字符串
     * @param size 汉字个数，eg:size=5，则将str在5个汉字的长度里两端对齐
     * @Return
     */
    private fun justifyString(str: String, size: Int): SpannableStringBuilder? {
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