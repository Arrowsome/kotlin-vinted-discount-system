package me.arrowsome.vinted_hw

data class DiscountedPack(
    val pack: Pack,
    val discountAmount: Float,
    val newPrice: Float,
)
