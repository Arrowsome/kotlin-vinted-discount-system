package me.arrowsome.vinted_hw.spec

import me.arrowsome.vinted_hw.Pack
import me.arrowsome.vinted_hw.data.DiscountDao
import me.arrowsome.vinted_hw.data.ProviderDao

abstract class DiscountSpec(
    protected val providerDao: ProviderDao,
    protected val discountDao: DiscountDao,
) {
    abstract fun calculateDiscount(pack: Pack): Discount

    protected fun Pack.applyRule(f: (Pack) -> Discount): Discount {
        return f(this)
    }

    protected fun Discount.applyRule(f: (Discount) -> Discount): Discount {
        return f(this)
    }
}