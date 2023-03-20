package me.arrowsome.vinted_hw.spec

import me.arrowsome.vinted_hw.Pack
import me.arrowsome.vinted_hw.PackSize
import me.arrowsome.vinted_hw.data.DiscountDao
import me.arrowsome.vinted_hw.data.ProviderDao

abstract class DefaultDiscountSpec(
    providerDao: ProviderDao,
    discountDao: DiscountDao,
) : DiscountSpec(providerDao, discountDao), SizeRule, BudgetRule {

    override fun calculateDiscount(pack: Pack): Discount {
        return pack
            .applyRule(::applySizeRule)
            .applyRule(::applyBudgetRule)
    }

    private fun applySizeRule(pack: Pack): Discount {
        return when (pack.size) {
            PackSize.SM -> applySmRule(pack)
            PackSize.MD -> applyMdRule(pack)
            PackSize.LG -> applyLgRule(pack)
        }
    }

    override fun applySmRule(pack: Pack): Discount {
        val fee = providerDao.findFee(pack.provider, pack.size)
            ?: return Discount.Corrupted
        val minFee = providerDao.findLowestFee(pack.size)

        val disAmount = fee - minFee!!

        return Discount.Applied(
            pack = pack,
            discountAmount = disAmount,
            basePrice = fee,
        )
    }

    override fun applyMdRule(pack: Pack): Discount {
        return Discount.NonApplicable
    }

    override fun applyLgRule(pack: Pack): Discount {
        return Discount.NonApplicable
    }

    override fun applyBudgetRule(discount: Discount): Discount {
        return when (discount) {
            is Discount.Applied -> {
                val sumOfMonthDis = discountDao.getDiscountSumByMonth(discount.pack.date.monthValue)
                var discountAmount = discount.discountAmount
                if (sumOfMonthDis >= 10f)
                    return Discount.NonApplicable
                else if ((sumOfMonthDis + discountAmount) > 10f)
                    discountAmount -= ((sumOfMonthDis + discountAmount) - 10f)

                return discount.copy(
                    discountAmount = discountAmount
                )
            }
            else -> discount
        }
    }

}