package me.arrowsome.vinted_hw.spec

//data class Discount(
//    val status: DiscountStatus,
//    val amount: Float,
//    val fromPrice: Float,
//) {
//    val toPrice: Float
//        get() = fromPrice - amount
//}
//
//enum class DiscountStatus {
//    APPLIED,
//    NON_APPLICABLE,
//    CORRUPTED,
//}

sealed class Discount {
    data class Applied(
        val discountAmount: Float,
        val basePrice: Float,
        ) : Discount() {
            val newPrice: Float
                get() = basePrice - discountAmount
        }

    object NonApplicable : Discount()

    object Corrupted : Discount()
}
