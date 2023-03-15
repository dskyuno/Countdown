package com.project.timemanagerment.feature.profile.data.respository

import com.project.timemanagerment.feature.profile.data.datasource.network.api.VipPriceServer
import com.project.timemanagerment.feature.profile.data.datasource.network.model.entity.NetVipPriceResponse
import javax.inject.Inject

class VipPriceRepositoryImpl @Inject constructor(val vipPriceServer: VipPriceServer):VipPriceRepository {
    override suspend fun getVipPrice(): NetVipPriceResponse {
       return vipPriceServer.getVipPrice()
    }
}