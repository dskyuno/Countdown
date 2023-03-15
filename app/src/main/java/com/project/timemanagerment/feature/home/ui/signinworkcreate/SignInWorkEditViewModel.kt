package com.project.timemanagerment.feature.home.ui.signinworkcreate

import androidx.lifecycle.*
import com.project.timemanagerment.base.util.SignInIconList
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInWork
import com.project.timemanagerment.feature.home.data.repository.SignInWorkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInWorkEditViewModel @Inject constructor(
    private val signInWorkRepository: SignInWorkRepository,

    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val signInWorkId = savedStateHandle.get<Long>(SIN_IN_WORK_ID)!!

    val signInWork = signInWorkRepository.getSignInWorkByPrimaryKey(signInWorkId).asLiveData()


    suspend fun getSignInWorkByPrimaryKeyNotFlow(signInWorkId: Long): SignInWork? {
        return signInWorkRepository.getSignInWorkByPrimaryKeyNotFlow(signInWorkId)
    }

    val signInWorkIconList = MutableLiveData(SignInIconList.getAllSignInIcon().toMutableList())

    companion object {
        private const val SIN_IN_WORK_ID = "signInWorkId"
    }


    fun createSignInWork(signInWork: SignInWork) {
        viewModelScope.launch {
            signInWorkRepository.insert(signInWork)
        }
    }

    fun updateSignInWork(work: SignInWork) {
        viewModelScope.launch {
            signInWorkRepository.update(work)
        }
    }
}