package com.project.timemanagerment.feature.home.ui.signinwork

import androidx.lifecycle.*
import com.project.timemanagerment.base.util.DateFormatUtil
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInDate
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInWork
import com.project.timemanagerment.feature.home.data.repository.SignInDateRepository
import com.project.timemanagerment.feature.home.data.repository.SignInWorkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SignInWorkViewModelNotStats @Inject constructor(
    private val signInWorkRepository: SignInWorkRepository,
    private val signInDateRepository: SignInDateRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val signInWorkList =
        signInWorkRepository.getSignInWorksByOrderIndexDesc()
            .asLiveData() as MutableLiveData

    fun createSignInWork(signInWork: SignInWork) {
        viewModelScope.launch {
            signInWorkRepository.insert(signInWork)
        }

    }

    fun insertSignInDateAndWorkFinalDate(signInDate: SignInDate) {
        viewModelScope.launch {
            val dateId = signInDateRepository.insert(signInDate)
            val signInWork =
                signInWorkRepository.getSignInWorkByPrimaryKeyNotFlow(signInDate.parentWorkId)
            val nowDateString = DateFormatUtil.getFormatYMD(Date().time)
            val signInDateString = DateFormatUtil.getFormatYMD(signInDate.dateTime.time)
            if (nowDateString == signInDateString) {
                signInWork!!.finalSignInDateTime = signInDate.dateTime
                signInWork.finalSignInDateId = dateId
                signInWorkRepository.update(signInWork)
            }
        }
    }

    fun deleteSignInDateByIdAndWorkFinalDate(signInDateId: Long, signInWorkId: Long) {
        viewModelScope.launch {
            val signInDate = signInDateRepository.getSignInDateByPrimaryKeyNotFlow(signInDateId)
            signInDate?.let {
                signInDateRepository.delete(it)
            }
            val signInWork = signInWorkRepository.getSignInWorkByPrimaryKeyNotFlow(signInWorkId)
            signInWork?.let {
                it.finalSignInDateTime = null
                it.finalSignInDateId = null
                signInWorkRepository.update(it)
            }
        }


    }

    fun updateSignInWork(signInWork: SignInWork) {
        viewModelScope.launch {
            signInWorkRepository.update(signInWork)
        }
    }

    fun updateSignInDate(signInDate: SignInDate) {
        viewModelScope.launch {
            signInDateRepository.update(signInDate)
        }
    }

    fun updateSignInDateIntroduction(signInWorkId: Long, introduction: String) {
        viewModelScope.launch {
            signInDateRepository.updateRecentSignInDateIntroduction(signInWorkId, introduction)
        }

    }
}