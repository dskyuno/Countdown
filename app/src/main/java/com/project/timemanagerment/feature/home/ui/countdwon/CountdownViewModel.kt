package com.project.timemanagerment.feature.home.ui.countdwon

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.Countdown
import com.project.timemanagerment.feature.home.data.repository.CountdownRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class CountdownViewModel @Inject constructor(
    private val countdownRepository: CountdownRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val countdownList =
        countdownRepository.getCountdownsByOrderIndexDesc().asLiveData() as MutableLiveData
    val updateCount = MutableLiveData(1)


    val items: Flow<PagingData<Countdown>> = Pager(
        config = PagingConfig(pageSize = 1, enablePlaceholders = false),
        pagingSourceFactory = { countdownRepository.getCountdownsByOrderIndexDescWithPaging() }
    )
        .flow
        // cachedIn allows paging to remain active in the viewModel scope, so even if the UI
        // showing the paged data goes through lifecycle changes, pagination remains cached and
        // the UI does not have to start paging from the beginning when it resumes.
        .cachedIn(viewModelScope)

    fun insertCountdown(countdown: Countdown) {
        viewModelScope.launch {
            countdownRepository.insertCountdown(countdown)
        }
    }

    suspend fun haveFreeNum(): Boolean {
        val freeNum = Countdown.freeNum
        val alreadyNum = countdownRepository.getCountdownCount()
        return freeNum > alreadyNum
    }

    fun updateCountdown(countdown: Countdown) {
        viewModelScope.launch {
            countdownRepository.updateCountdown(countdown)
        }
    }

    fun getCountdownById(countdownId: Long): Flow<Countdown> {
        return countdownRepository.getCountDownById(countdownId)
    }

    suspend fun getCountdownByIdNotFlow(countdownId: Long): Countdown {
        return countdownRepository.getCountDownByIdNotFlow(countdownId)
    }

    fun move(from: Int, to: Int) {
        countdownList.value?.let { list ->
            val countdown = list.removeAt(from)
            list.add(to, countdown)
            countdownList.value = list
        }
        val newList = countdownList.value!!.toMutableList()
        for (element in newList.size downTo 1) {
            newList[newList.size - element].orderIndex = element.toLong()
        }
    }

    fun proNullAnSetList() {
        val tempValue = countdownList.value
        countdownList.value = mutableListOf()
        countdownList.value = tempValue
    }

    fun addIndex() {
        updateCount.value = updateCount.value?.plus(1)
    }

    fun updateTopCountdown() {
        val newTopCountdown = topCountdown.value?.copy()
        if (newTopCountdown != null) {
            newTopCountdown.countdownId = topCountdown.value?.countdownId ?: 0
        }
        topCountdown.value = newTopCountdown
    }

    val topCountdown = countdownRepository.getMaxIndexCountdown().asLiveData() as MutableLiveData
}