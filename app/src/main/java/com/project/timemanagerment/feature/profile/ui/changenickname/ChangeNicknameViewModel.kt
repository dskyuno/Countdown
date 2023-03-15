package com.project.timemanagerment.feature.profile.ui.changenickname

import androidx.lifecycle.ViewModel
import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.BaseResponse
import com.project.timemanagerment.feature.profile.data.respository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeNicknameViewModel @Inject constructor(val userRepository: UserRepository) :
    ViewModel() {

    suspend fun changeNickname(nickname: String): BaseResponse<String> {
        return userRepository.changeNickname(nickname)
    }
}