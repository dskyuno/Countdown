package com.project.timemanagerment.feature.profile.data.respository

import com.google.gson.Gson
import com.project.timemanagerment.feature.profile.data.datasource.network.api.UserServer
import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.BaseResponse
import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.NetUser
import com.project.timemanagerment.feature.profile.data.datasource.network.model.requestbean.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userServer: UserServer) : UserRepository {
    override suspend fun getUser(userId: Long): BaseResponse<NetUser>? {
        return try {
            userServer.getUser(userId)
        } catch (exception: Exception) {
            null
        }
    }

    override suspend fun login(loginBean: LoginBean): BaseResponse<String> {
        return withContext(Dispatchers.IO) {
            try {
                userServer.userLogin(
                    loginBean
                )
            } catch (exception: Exception) {
                cacheException<String>(exception)
            }
        }
    }

    override suspend fun getUserInfo(): BaseResponse<NetUser> {
        return try {
            userServer.getUserInfo()
        } catch (exception: Exception) {
            cacheException<NetUser>(exception)
        }
    }

    override suspend fun reLogin(): BaseResponse<NetUser> {
        return try {
            userServer.reLogin()
        } catch (exception: Exception) {
            cacheException<NetUser>(exception)
        }
    }

    override suspend fun changeNickname(nickname: String): BaseResponse<String> {
        return try {
            val nicknameBean = ChangeNicknameBean(nickname)
            userServer.changeNickName(nicknameBean)
        } catch (exception: Exception) {
            cacheException<String>(exception)
        }
    }

    override suspend fun feedback(feedbackBean: FeedbackBean): BaseResponse<String> {
        return try {
            userServer.feedback(feedbackBean)
        } catch (exception: Exception) {
            cacheException<String>(exception)
        }
    }

    override suspend fun forgetPassword(forgetPasswordBean: ForgetPasswordBean): BaseResponse<String> {
        return try {
            userServer.forgetPassword(forgetPasswordBean)
        } catch (exception: Exception) {
            cacheException<String>(exception)
        }
    }

    override suspend fun registerAccount(registerAccountBean: RegisterAccountBean): BaseResponse<String> {
        return try {
            userServer.register(registerAccountBean)
        } catch (exception: Exception) {
            cacheException<String>(exception)
        }
    }

    private inline fun <reified T : Any> cacheException(exception: Exception): BaseResponse<T> {
        return when (exception) {
            is SocketTimeoutException -> BaseResponse<T>(401, "网络连接超时，请联系管理员", null)
            is ConnectException -> BaseResponse<T>(402, "无网络连接", null)
            else -> BaseResponse<T>(403, "未知错误，请联系管理员", null)
        }
    }


}