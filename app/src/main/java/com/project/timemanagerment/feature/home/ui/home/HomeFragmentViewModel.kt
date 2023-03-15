package com.project.timemanagerment.feature.home.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

   /* val users = userRepository.selectALlUser().asLiveData()
    fun insertUser(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }*/
}