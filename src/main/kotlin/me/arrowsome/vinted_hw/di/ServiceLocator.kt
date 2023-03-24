package me.arrowsome.vinted_hw.di

import me.arrowsome.vinted_hw.data.CourierDao
import me.arrowsome.vinted_hw.data.ShipmentDao
import me.arrowsome.vinted_hw.rule.MonthlyLimit
import me.arrowsome.vinted_hw.rule.RuleFactory
import me.arrowsome.vinted_hw.rule.SmMatchMinFee
import me.arrowsome.vinted_hw.rule.ThirdLgOnTheHouse

@Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
object ServiceLocator {
    // DAOs
    private val courierDao by lazy { CourierDao() }
    private val shipmentDao by lazy { ShipmentDao() }

    // Rules
    private val ruleFactory by lazy { RuleFactory }
    private val smMatchMinFee by lazy { SmMatchMinFee(courierDao) }
    private val thirdLgOnTheHouse by lazy { ThirdLgOnTheHouse(shipmentDao) }
    private val monthlyLimit by lazy { MonthlyLimit(shipmentDao) }

    inline fun <reified T> get(): T {
        return when (T::class) {
            CourierDao::class -> courierDao as T
            ShipmentDao::class -> shipmentDao as T
            RuleFactory::class -> ruleFactory as T
            SmMatchMinFee::class -> smMatchMinFee as T
            ThirdLgOnTheHouse::class -> thirdLgOnTheHouse as T
            MonthlyLimit::class -> monthlyLimit as T
            else -> throw IllegalArgumentException("No instance was found for ${T::class.simpleName}")
        }
    }
}