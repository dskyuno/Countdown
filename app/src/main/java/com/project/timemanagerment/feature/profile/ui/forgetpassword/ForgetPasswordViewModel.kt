package com.project.timemanagerment.feature.profile.ui.forgetpassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.facebook.stetho.Stetho
import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.BaseResponse
import com.project.timemanagerment.feature.profile.data.datasource.network.model.requestbean.ForgetPasswordBean
import com.project.timemanagerment.feature.profile.data.respository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
  val   userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {
    suspend fun forgetPassword(forgetPasswordBean: ForgetPasswordBean): BaseResponse<String> {
        return  userRepository.forgetPassword(forgetPasswordBean)
    }
}