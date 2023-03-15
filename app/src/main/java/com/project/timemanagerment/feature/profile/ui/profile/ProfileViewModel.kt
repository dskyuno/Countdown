package com.project.timemanagerment.feature.profile.ui.profile


import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.project.timemanagerment.UserPreferences
import com.project.timemanagerment.base.constant.Constants
import com.project.timemanagerment.base.constant.ConstantsPreferencesKey
import com.project.timemanagerment.base.datestore.DataStorePreferencesRepository
import com.project.timemanagerment.base.datestore.proto.UserPreferencesRepository
import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.BaseResponse
import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.NetUser
import com.project.timemanagerment.feature.profile.data.respository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStorePreferencesRepository: DataStorePreferencesRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    suspend fun getUser(userId: Long): BaseResponse<NetUser>? {
        return userRepository.getUser(userId)
    }
    suspend fun getUserPreferenceSync(): UserPreferences {
        return userPreferencesRepository.getUserPreferenceSync()
    }

    /*   suspend fun getUserInfoFromDataStore():NetUser? {
           val preferenceString = dataStorePreferencesRepository.getStringPreferencesSync(
               ConstantsPreferencesKey.userToken,
               ConstantsPreferencesKey.userToken_default_value
           )
           if(preferenceString.isNullOrEmpty()){
               return
           }
           //   if()

       }*/

}