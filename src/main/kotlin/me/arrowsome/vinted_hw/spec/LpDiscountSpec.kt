package me.arrowsome.vinted_hw.spec

import me.arrowsome.vinted_hw.*
import me.arrowsome.vinted_hw.data.DiscountDao
import me.arrowsome.vinted_hw.data.ProviderDao

class LpDiscountSpec(
    private val providerDao: ProviderDao,
    private val discountDao: DiscountDao,
) {

    fun calculateDiscount(pack: Pack): Discount {
        return when (pack.size) {
            PackSize.SM -> calcSmPackDiscount(pack)
            PackSize.MD -> calcMdPackDiscount(pack)
            PackSize.LG -> calcLgPackDiscount(pack)
        }
    }

    private fun calcSmPackDiscount(pack: Pack): Discount {
        val fee = providerDao.findFee(pack.provider, pack.size)
            ?: return Discount.Corrupted
        val minFee = providerDao.findLowestFee(pack.size)

        var disAmount = fee - minFee!!

        val sumOfMonthDis = discountDao.getDiscountSumByMonth(pack.date.monthValue)
        if (sumOfMonthDis >= 10f)
            return Discount.NonApplicable
        else if ((sumOfMonthDis + disAmount) > 10f)
            disAmount -= ((sumOfMonthDis + disAmount) - 10f)

        return Discount.Applied(
            discountAmount = disAmount,
            basePrice = fee,
        )
    }

    private fun calcMdPackDiscount(pack: Pack): Discount {
        return Discount.NonApplicable
    }

    private fun calcLgPackDiscount(pack: Pack): Discount {
        val fee = providerDao.findFee(pack.provider, pack.size)
            ?: return Discount.Corrupted
        val count = discountDao.getDiscountCountInMonthBySize(
            pack.date.monthValue,
            pack.size,
        )

        var disAmount = if (count == 3) fee else 0f

        val sumOfMonthDis = discountDao.getDiscountSumByMonth(pack.date.monthValue)
        if (sumOfMonthDis >= 10f)
            return Discount.NonApplicable
        else if ((sumOfMonthDis + disAmount) > 10f)
            disAmount -= ((sumOfMonthDis + disAmount) - 10f)

        return Discount.Applied(
            discountAmount = disAmount,
            basePrice = fee,
        )
    }

}