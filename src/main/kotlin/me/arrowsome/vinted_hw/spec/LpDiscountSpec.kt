package me.arrowsome.vinted_hw.spec

import me.arrowsome.vinted_hw.*
import me.arrowsome.vinted_hw.data.DiscountDao
import me.arrowsome.vinted_hw.data.ProviderDao

class LpDiscountSpec(
    private val providerDao: ProviderDao,
    private val discountDao: DiscountDao,
) {

    fun calculateDiscount(pack: Pack): Result<DiscountedPack, NoDiscountStatus> {
        return when (pack.size) {
            PackSize.SM -> calcSmPackDiscount(pack)
            PackSize.MD -> calcMdPackDiscount(pack)
            PackSize.LG -> calcLgPackDiscount(pack)
        }
    }

    private fun calcSmPackDiscount(pack: Pack): Result<DiscountedPack, NoDiscountStatus> {
        val fee = providerDao.findFee(pack.provider, pack.size)
            ?: return Result.Failure(NoDiscountStatus.IGNORED)
        val minFee = providerDao.findLowestFee(pack.size)

        var disAmount = fee - minFee!!

        val sumOfMonthDis = discountDao.getDiscountSumByMonth(pack.date.monthValue)
        if (sumOfMonthDis >= 10f)
            return Result.Failure(NoDiscountStatus.NON_APPLICABLE)
        else if ((sumOfMonthDis + disAmount) > 10f)
            disAmount -= ((sumOfMonthDis + disAmount) - 10f)

        val newPrice = fee - disAmount

        return Result.Success(
            DiscountedPack(
                pack = pack,
                discountAmount = disAmount,
                newPrice = newPrice,
            )
        )
    }

    private fun calcMdPackDiscount(pack: Pack): Result<DiscountedPack, NoDiscountStatus> {
        return Result.Failure(NoDiscountStatus.IGNORED)
    }

    private fun calcLgPackDiscount(pack: Pack): Result<DiscountedPack, NoDiscountStatus> {
        val fee = providerDao.findFee(pack.provider, pack.size)
            ?: return Result.Failure(NoDiscountStatus.IGNORED)
        val count = discountDao.getDiscountCountInMonthBySize(
            pack.date.monthValue,
            pack.size,
        )

        var disAmount = if (count == 3) fee else 0f

        val sumOfMonthDis = discountDao.getDiscountSumByMonth(pack.date.monthValue)
        if (sumOfMonthDis >= 10f)
            return Result.Failure(NoDiscountStatus.NON_APPLICABLE)
        else if ((sumOfMonthDis + disAmount) > 10f)
            disAmount -= ((sumOfMonthDis + disAmount) - 10f)

        val newPrice = fee - disAmount

        return Result.Success(DiscountedPack(
            pack = pack,
            discountAmount = disAmount,
            newPrice = newPrice,
        ))
    }

}