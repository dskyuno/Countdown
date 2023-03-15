package com.project.timemanagerment.feature.home.ui.countdowndetail

import androidx.lifecycle.*
import com.project.timemanagerment.base.util.CountdownThemeList
import com.project.timemanagerment.base.util.TextTypefaceList
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.Countdown
import com.project.timemanagerment.feature.home.data.repository.CountdownRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountdownDetailViewModel @Inject constructor(
    private val countdownRepository: CountdownRepository,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    val countdownId = savedStateHandle.get<Long>(COUNTDOWN_ID)!!
    val countdown =
        countdownRepository.getCountDownById(countdownId).asLiveData() as MutableLiveData

    suspend fun getGetCountDownNotFlow(): Countdown {
        return countdownRepository.getCountDownByIdNotFlow(countdownId)
    }

    val textTypefaceList = MutableLiveData(TextTypefaceList.getAllTextTypeface().toMutableList())
    val themeList = MutableLiveData(CountdownThemeList.getThemeList().toMutableList())

    companion object {
        private const val COUNTDOWN_ID = "countdownId"
    }

    fun deleteCountdown() {
        viewModelScope.launch {
            countdown.value?.let { countdownRepository.deleteCountdown(it) }
        }

    }

    fun refreshCountdown() {
        viewModelScope.launch {
            countdown.value = countdownRepository.getCountDownByIdNotFlow(countdownId);
        }
    }

    fun updateBackground(fontIndex: Int, themeIndex: Int) {
        viewModelScope.launch {
            val countdown = countdownRepository.getCountDownByIdNotFlow(countdownId);
            countdown.textFontIndex = fontIndex
            countdown.themeIndex = themeIndex
            countdownRepository.updateCountdown(countdown)
        }
    }

    fun itemToTop() {
        viewModelScope.launch {
            val maxIndex = countdownRepository.getMaxOrderIndexInCountdown()
            val countdown = countdownRepository.getCountDownByIdNotFlow(countdownId)
            if (maxIndex != null) {
                countdown.orderIndex = maxIndex + 1
                countdownRepository.updateCountdown(countdown)
            }

        }
    }

    fun copyUpdateCurrentCountdown() {
        val newCountdown = countdown.value?.copy()
        if (newCountdown != null) {
            newCountdown.countdownId = countdown.value?.countdownId ?: 0
        }
        countdown.value = newCountdown

    }

}