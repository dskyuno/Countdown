package com.project.timemanagerment.app.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.project.timemanagerment.UserPreferences
import com.project.timemanagerment.base.datestore.DataStorePreferencesRepository
import com.project.timemanagerment.base.datestore.proto.UserPreferencesRepository
import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.BaseResponse
import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.NetUser
import com.project.timemanagerment.feature.profile.data.respository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    val userRepository: UserRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val dataStorePreferencesRepository: DataStorePreferencesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    suspend fun  reLogin(): BaseResponse<NetUser> {
        return userRepository.reLogin()
    }

    suspend fun updateUserPreferences(userPreferences: UserPreferences?) {
        if (userPreferences != null) {
            userPreferencesRepository.setUserPreference(userPreferences)
        }
    }


}