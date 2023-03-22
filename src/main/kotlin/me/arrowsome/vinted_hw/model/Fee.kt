package me.arrowsome.vinted_hw.model

data class Fee(
    val base: Float,
    val discount: Float = 0f,
) {
    val payable: Float
        get() =
            if (discount >= 0)
                base - discount
            else base

    val discountStatus: DiscountStatus
        get() = when {
            discount == 0f -> DiscountStatus.NON_APPLICABLE
            discount > 0f  -> DiscountStatus.APPLIED
            else -> DiscountStatus.IGNORED
        }
}