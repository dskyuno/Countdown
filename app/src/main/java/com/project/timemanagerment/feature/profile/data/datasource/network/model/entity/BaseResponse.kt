package com.project.timemanagerment.feature.profile.data.datasource.network.model.entity

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @field:SerializedName("code") val code: Int,
    @field:SerializedName("message") val msg: String,
    @field:SerializedName("data") val data: T?
) {

}