package me.arrowsome.vinted_hw.spec

import me.arrowsome.vinted_hw.Pack

sealed class Discount {
    data class Applied(
        val pack: Pack,
        val discountAmount: Float,
        val basePrice: Float,
        ) : Discount() {
            val newPrice: Float
                get() = basePrice - discountAmount
        }

    object NonApplicable : Discount()

    object Corrupted : Discount()
}
