package me.arrowsome.vinted_hw.spec

import me.arrowsome.vinted_hw.*
import me.arrowsome.vinted_hw.data.DiscountDao
import me.arrowsome.vinted_hw.data.ProviderDao

class LpDiscountSpec(
    providerDao: ProviderDao,
    discountDao: DiscountDao,
) : DefaultDiscountSpec(providerDao, discountDao) {

    override fun applyLgRule(pack: Pack): Discount {
        val fee = providerDao.findFee(pack.provider, pack.size)
            ?: return Discount.Corrupted
        val count = discountDao.getDiscountCountInMonthBySize(
            pack.date.monthValue,
            pack.size,
        )

        val discountAmount = if (count == 3) fee else 0f

        return Discount.Applied(
            pack = pack,
            discountAmount = discountAmount,
            basePrice = fee,
        )
    }

}