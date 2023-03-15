package com.project.timemanagerment.feature.profile.data.datasource.network.model.entity

import com.google.gson.annotations.SerializedName

data class NetVipPriceResponse(
    @field:SerializedName("code") val code: Int,
    @field:SerializedName("message") val message: String,
    @field:SerializedName("vipPriceList") val  vipPriceList:MutableList<NetVipPrice>
)
