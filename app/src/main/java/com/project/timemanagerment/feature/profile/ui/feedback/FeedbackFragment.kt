package com.project.timemanagerment.feature.profile.ui.feedback

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.StringUtils
import com.project.timemanagerment.base.di.AppInfo
import com.project.timemanagerment.base.util.ToastUtilsDraK
import com.project.timemanagerment.base.util.loadingDialogXPopup
import com.project.timemanagerment.databinding.FragmentFeedbackBinding
import com.project.timemanagerment.feature.profile.data.datasource.network.model.requestbean.FeedbackBean
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class FeedbackFragment : Fragment() {
    lateinit var binding: FragmentFeedbackBinding
    val viewModel: FeedbackViewModel by viewModels()

    @Inject
    lateinit var appInfo: AppInfo
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        initListener()
        //feedbackTextCount
        binding.tvTextCount.text =
            binding.etUserFeedback.text.toString().length.toString() + "/" + "200"
        binding.etUserFeedback.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                binding.tvTextCount.text =
                    binding.etUserFeedback.text.toString().length.toString() + "/" + "200"
            }

        })
    }

    private fun setToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initListener() {
        binding.tvFeedbackSubmit.setOnClickListener {
            if (handlerUserInput()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    val loadingDialog = loadingDialogXPopup(requireContext())
                    val feedbackBean = FeedbackBean(
                        binding.etUserFeedback.toString(),
                        binding.etEmail.toString(),
                        appInfo.systemVersion,
                        appInfo.phoneBrand,
                        appInfo.phoneBrand
                    )
                    val response = viewModel.userFeedback(feedbackBean)
                    when (response.code) {
                        200 -> {
                            loadingDialog.dismiss()
                            ToastUtilsDraK.showMsgCenter(response.msg)
                        }
                        400 -> {

                            loadingDialog.dismiss()
                            ToastUtilsDraK.showMsgCenter(response.msg)
                        }
                        else -> {
                            loadingDialog.dismiss()
                            ToastUtilsDraK.showMsgCenter(response.msg+response.code)
                        }
                    }
                }


            }

        }
    }

    private fun isValidEmailFormat(email: String?): Boolean { //邮箱判断正则表达式
        val pattern = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")
        val mc = pattern.matcher(email)
        return mc.matches()
    }

    private fun isEmpty(s: String): Boolean {
        return StringUtils.isEmpty(s) || s.trim { it <= ' ' }.isEmpty()
    }


    private fun handlerUserInput(): Boolean {
        return if (binding.etUserFeedback.text.toString().length < 10) {
            ToastUtilsDraK.showMsgTop("反馈字数请大于10")
            false
        } else if (binding.etUserFeedback.text.toString().length > 200) {
            ToastUtilsDraK.showMsgTop("单次反馈字数请小于200")
            false
        } else if (isEmpty(binding.etEmail.text.toString())) {
            ToastUtilsDraK.showMsgTop("请输入邮箱方面我们联系")
            false
        } else if (!isValidEmailFormat(binding.etEmail.text.toString())) {
            ToastUtilsDraK.showMsgTop("邮箱格式不正确")
            false
        } else {
            true
        }
    }


}