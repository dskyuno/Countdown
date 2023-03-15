package com.project.timemanagerment.feature.profile.ui.feedback

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.BaseResponse
import com.project.timemanagerment.feature.profile.data.datasource.network.model.requestbean.FeedbackBean
import com.project.timemanagerment.feature.profile.data.respository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    suspend fun userFeedback(feedbackBean: FeedbackBean): BaseResponse<String> {
        return userRepository.feedback(feedbackBean)
    }
}