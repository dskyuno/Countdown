package com.project.timemanagerment.feature.home.ui.countdownsave

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.timemanagerment.base.util.CountdownThemeList
import com.project.timemanagerment.base.util.TextTypefaceList
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.Countdown
import com.project.timemanagerment.feature.home.data.repository.CountdownRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CountdownSaveImageViewModel @Inject constructor(
    private val countdownRepository: CountdownRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val countdownId = savedStateHandle.get<Long>(COUNTDOWN_ID)!!
    val countdown =
        countdownRepository.getCountDownById(countdownId).asLiveData() as MutableLiveData

    val textTypefaceList = MutableLiveData(TextTypefaceList.getAllTextTypeface().toMutableList())
    val themeList = MutableLiveData(CountdownThemeList.getThemeList().toMutableList())

    suspend fun getGetCountDownNotFlow(): Countdown {
        return countdownRepository.getCountDownByIdNotFlow(countdownId)
    }

    companion object {
        private const val COUNTDOWN_ID = "countdownId"
    }

}