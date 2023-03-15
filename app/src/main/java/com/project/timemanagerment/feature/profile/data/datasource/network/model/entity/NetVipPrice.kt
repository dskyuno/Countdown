package com.project.timemanagerment.feature.profile.data.datasource.network.model.entity

import com.google.gson.annotations.SerializedName

data class NetVipPrice(
    @field:SerializedName("vipPriceId")  val vipPriceId: Long,
    @field:SerializedName("continueTimeString")   val continueTimeString: String,
    @field:SerializedName("currentPrice")    val currentPrice: Int,
    @field:SerializedName("originalPrice")    val originalPrice: Int,
    @field:SerializedName("isPromotion")     val isPromotion: Boolean

)
