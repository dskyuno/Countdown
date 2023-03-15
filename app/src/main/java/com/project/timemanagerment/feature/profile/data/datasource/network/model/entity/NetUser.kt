package com.project.timemanagerment.feature.profile.data.datasource.network.model.entity

import com.google.gson.annotations.SerializedName
import java.util.*

class NetUser(
    @field:SerializedName("account") val account: String,
    @field:SerializedName("email") val email: String,
    @field:SerializedName("nickname") val nickname: String,
    @field:SerializedName("sex") val sex: Int,
    @field:SerializedName("vipExpireTime") val vipExpireTime: Date
)