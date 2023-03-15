package com.project.timemanagerment.feature.home.ui.signindate

import androidx.lifecycle.*
import com.project.timemanagerment.base.util.DateFormatUtil
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInDate
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInWork
import com.project.timemanagerment.feature.home.data.repository.SignInDateRepository
import com.project.timemanagerment.feature.home.data.repository.SignInWorkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SignInDateViewModel @Inject constructor(
    private val signInWorkRepository: SignInWorkRepository,
    private val signInDateRepository: SignInDateRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val parentWorkId = savedStateHandle.get<Long>(PARENT_WORK_ID)!!
    val signInWork = signInWorkRepository.getSignInWorkByPrimaryKey(parentWorkId!!)
        .asLiveData() as MutableLiveData

    val signInDateList = signInDateRepository.getSignInDatesByParentWorkId(parentWorkId!!)
        .asLiveData() as MutableLiveData


    //if have endTime stats before EndTime
    /*    val sinInDateSize = signInDateRepository.getDatesSizeByParentWorkId(parentWorkId!!)
            .asLiveData() as MutableLiveData*/
    val sinInDateSize = MutableStateFlow<Int>(0)


    //if have endTime stats before EndTime

    val signInDateByDateTimeAsc = MutableStateFlow(mutableListOf<SignInDate>())

    /*val signInDateByDateTimeAsc =
        signInDateRepository.getSignInDatesByParentWorkIdOrderByDateTimeAsc(parentWorkId!!)
            .asLiveData() as MutableLiveData*/

    init {
        updateStats()
    }

    fun updateStats() {
        viewModelScope.launch {
            val work = signInWorkRepository.getSignInWorkByPrimaryKeyNotFlow(parentWorkId!!)
            if (work!!.endTimeActive) {
                //signIn continues
                signInDateByDateTimeAsc.emitAll(
                    signInDateRepository.getSignInDatesByParentWorkIdOrderByDateTimeAsc(
                        parentWorkId,
                        work.endTime
                    )
                )
            } else {
                //signIn continues
                signInDateByDateTimeAsc.emitAll(
                    signInDateRepository.getSignInDatesByParentWorkIdOrderByDateTimeAsc(
                        parentWorkId,
                        work.endTime
                    )
                )
            }
        }

        viewModelScope.launch {

            val work = signInWorkRepository.getSignInWorkByPrimaryKeyNotFlow(parentWorkId!!)
            if (work!!.endTimeActive) {
                //sum signIn
                sinInDateSize.emitAll(
                    signInDateRepository.getDatesSizeByParentWorkId(
                        parentWorkId,
                        work.endTime
                    )
                )
            } else {
                //sum signIn
                sinInDateSize.emitAll(
                    signInDateRepository.getDatesSizeByParentWorkId(
                        parentWorkId,
                        null
                    )
                )
            }
        }
    }


    fun insertSignInDate(signInDate: SignInDate) {
        viewModelScope.launch {
            if (parentWorkId != null) {
                signInDate.parentWorkId = parentWorkId
                val dateId = signInDateRepository.insert(signInDate)
                val signInWork = signInWorkRepository.getSignInWorkByPrimaryKeyNotFlow(parentWorkId)
                val nowDateString = DateFormatUtil.getFormatYMD(Date().time)
                val signInDateString = DateFormatUtil.getFormatYMD(signInDate.dateTime.time)
                if (nowDateString == signInDateString) {
                    signInWork!!.finalSignInDateTime = signInDate.dateTime
                    signInWork.finalSignInDateId = dateId
                    signInWorkRepository.update(signInWork)
                }
            }
        }
    }

    fun deleteCurrentSignInWork() {
        viewModelScope.launch {
            val signInWork = signInWorkRepository.getSignInWorkByPrimaryKeyNotFlow(parentWorkId!!)
            val signInDates = signInDateRepository.getSignInDatesByParentWorkIdNotFlow(parentWorkId)
            signInWork?.let {
                signInWorkRepository.delete(signInWork)
            }
            if (signInDates.size > 0) {
                signInDateRepository.delete(*signInDates.toTypedArray())
            }
        }
    }

    suspend fun getSignInDateByParentAndDateTime(dateTime: Date): SignInDate? {
        return signInDateRepository.getSignInDateByParentAndDateTime(parentWorkId, dateTime)
    }

    fun deleteSignInDate(signInDate: SignInDate) {
        viewModelScope.launch {
            val signInWork = signInWorkRepository.getSignInWorkByPrimaryKeyNotFlow(parentWorkId!!)
            if (signInDate.signInDateId == signInWork!!.finalSignInDateId) {
                signInWork.finalSignInDateTime = null
                signInWork.finalSignInDateId = null
                signInWorkRepository.update(signInWork)
            }
            signInDateRepository.delete(signInDate)
        }
    }

    private fun todayIsSignIn(signInWork: SignInWork): Boolean {
        val nowDateString = DateFormatUtil.getFormatYMD(Date().time)
        val signInDateString =
            if (signInWork.finalSignInDateTime != null) DateFormatUtil.getFormatYMD(
                signInWork.finalSignInDateTime!!.time
            ) else ""
        return nowDateString == signInDateString
    }

    fun updateSignInDate(signInDateAlready: SignInDate) {
        viewModelScope.launch {
            signInDateRepository.update(signInDateAlready)
        }
    }

    fun copyUpdateCurrentWork() {
        val newWork = signInWork.value?.copy()
        if (newWork != null) {
            newWork.signInWorkId = signInWork.value?.signInWorkId ?: 0
        }
        signInWork.value = newWork
    }

    fun itemToTop() {
        viewModelScope.launch {
            val maxIndex = signInWorkRepository.getMaxOrderIndexInSignWork()
            val signInWork = signInWorkRepository.getSignInWorkByPrimaryKeyNotFlow(parentWorkId)
            maxIndex?.let {
                if (signInWork != null) {
                    signInWork.orderIndex = maxIndex+1
                    signInWorkRepository.update(signInWork)
                }
            }

        }
    }

    companion object {
        const val PARENT_WORK_ID = "parentWorkId"
    }

}