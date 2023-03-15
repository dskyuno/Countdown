package com.project.timemanagerment.feature.profile.data.respository

import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.BaseResponse
import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.NetUser
import com.project.timemanagerment.feature.profile.data.datasource.network.model.requestbean.FeedbackBean
import com.project.timemanagerment.feature.profile.data.datasource.network.model.requestbean.ForgetPasswordBean
import com.project.timemanagerment.feature.profile.data.datasource.network.model.requestbean.LoginBean
import com.project.timemanagerment.feature.profile.data.datasource.network.model.requestbean.RegisterAccountBean

interface UserRepository {
    suspend fun getUser(userId: Long): BaseResponse<NetUser>?
    suspend fun login(loginBean: LoginBean): BaseResponse<String>
    suspend fun getUserInfo(): BaseResponse<NetUser>
    suspend fun reLogin():BaseResponse<NetUser>
    suspend fun changeNickname(nickname: String): BaseResponse<String>
    suspend fun feedback(feedbackBean: FeedbackBean): BaseResponse<String>
    suspend fun forgetPassword(forgetPasswordBean: ForgetPasswordBean): BaseResponse<String>
    suspend fun registerAccount(registerAccountBean: RegisterAccountBean): BaseResponse<String>
}