package com.project.timemanagerment.feature.home.ui.signinwork.recyclerview.callback

import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInDate
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInWork

interface SignInWorkAdapterCallback {

    fun insertSignInDate(signInDate: SignInDate)
    fun deleteSignInDate(signDateId: Long, signInWorkId: Long)
    fun updateSignInWork(signInWork: SignInWork)
    fun updateSignInDate(signInDate: SignInDate)
    fun updateSignInDateIntroduction(signInWorkId: Long,introduction: String)
}