package com.project.timemanagerment.feature.profile.data.respository

import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.NetVipPriceResponse

interface VipPriceRepository {
    suspend fun getVipPrice(): NetVipPriceResponse
}