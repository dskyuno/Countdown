package com.project.timemanagerment.feature.profile.ui.login


import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.project.timemanagerment.base.datestore.DataStorePreferencesRepository
import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.BaseResponse
import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.NetUser
import com.project.timemanagerment.feature.profile.data.datasource.network.model.requestbean.LoginBean
import com.project.timemanagerment.feature.profile.data.respository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStorePreferencesRepository: DataStorePreferencesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    suspend fun login(loginBean: LoginBean): BaseResponse<String> {
        return userRepository.login(loginBean)
    }

    suspend fun setDataStoreToken(preferences: Preferences.Key<String>, token: String) {
        dataStorePreferencesRepository.setStringPreferences(preferences, token)
    }

    suspend fun getUserInfo(): BaseResponse<NetUser> {
        return userRepository.getUserInfo()
    }
}