package com.project.timemanagerment.feature.profile.ui.changenickname

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.project.timemanagerment.base.util.ToastUtilsDraK
import com.project.timemanagerment.base.util.loadingDialogXPopup
import com.project.timemanagerment.databinding.FragmentChangeNicknameBinding

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangeNicknameFragment : Fragment() {
    lateinit var binding: FragmentChangeNicknameBinding
    val viewModel: ChangeNicknameViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeNicknameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyBord(binding.etNickname)
        initListener()
    }

    private fun initListener() {
        binding.tvChangeNicknameSubmit.setOnClickListener {
            if (binding.etNickname.text.toString().isEmpty()) {
                ToastUtilsDraK.showMsgTop("昵称不能为空")
            } else if (binding.etNickname.text.toString().length > 20) {
                ToastUtilsDraK.showMsgTop("昵称需小于20")
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    val loadingDialog = loadingDialogXPopup(requireContext())
                    val response = viewModel.changeNickname(binding.etNickname.toString())
                    when (response.code) {
                        200 -> {
                            ToastUtilsDraK.showMsgCenter(response.msg)
                            loadingDialog.dismiss()
                        }
                        400 -> {
                            ToastUtilsDraK.showMsgCenter(response.msg)
                            loadingDialog.dismiss()
                        }
                        else -> {
                            ToastUtilsDraK.showMsgCenter(response.msg + response.code)
                            loadingDialog.dismiss()
                        }
                    }
                }

            }
        }
    }

    private fun showKeyBord(editText: EditText) {
        editText.postDelayed(Runnable {
            editText.requestFocus()
            val imm: InputMethodManager = editText.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm != null) imm.showSoftInput(
                editText,
                InputMethodManager.SHOW_IMPLICIT
            )
        }, 300)
    }
}