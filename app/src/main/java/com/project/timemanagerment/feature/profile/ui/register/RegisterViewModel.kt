package com.project.timemanagerment.feature.profile.ui.register

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.BaseResponse
import com.project.timemanagerment.feature.profile.data.datasource.network.model.requestbean.RegisterAccountBean
import com.project.timemanagerment.feature.profile.data.respository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    suspend fun registerAccount(registerAccountBean: RegisterAccountBean): BaseResponse<String> {
return  userRepository.registerAccount(registerAccountBean)
    }
}