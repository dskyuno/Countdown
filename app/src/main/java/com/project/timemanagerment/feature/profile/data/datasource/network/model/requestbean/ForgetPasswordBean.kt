package com.project.timemanagerment.feature.profile.data.datasource.network.model.requestbean

data class ForgetPasswordBean(
    val email: String,
    val verifyCode: String,
    val newPassword: String,
    val newPasswordAgain: String
)
