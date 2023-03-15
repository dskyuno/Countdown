package com.project.timemanagerment.base.util

data class VipPrice(
    val vipPriceId: Long,
    val continueTimeString: String,
    val currentPrice: Int,
    val originalPrice: Int,
    val isPromotion: Boolean
)

class MockVipPriceList {
    companion object {
        fun getMockPriceList(): List<VipPrice> {
            val list = mutableListOf<VipPrice>()
            for (i in 1..3) {
                val item = VipPrice(i.toLong(), i.toString() + "个月", i, i + 1, true)
                list.add(item)
            }
            return list
        }
    }

}