package com.project.timemanagerment.feature.profile.data.datasource.network.model.requestbean

data class RegisterAccountBean(
    val account: String,
    val email: String,
    val password: String,
    val passwordAgain: String
)
