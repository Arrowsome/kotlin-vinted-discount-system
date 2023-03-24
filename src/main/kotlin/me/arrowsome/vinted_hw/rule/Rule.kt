package me.arrowsome.vinted_hw.rule

import me.arrowsome.vinted_hw.data.CourierDao
import me.arrowsome.vinted_hw.data.ShipmentDao
import me.arrowsome.vinted_hw.di.ServiceLocator
import me.arrowsome.vinted_hw.model.Courier
import me.arrowsome.vinted_hw.model.Shipment

interface Rule {
    var nextRule: Rule?
    fun applyDiscount(shipment: Shipment): Shipment
}

abstract class BaseRule : Rule {
    override var nextRule: Rule? = null
}

object RuleFactory {
    private val smMonthlyLimit = ServiceLocator.get<SmMatchMinFee>()
    private val thirdLgOnTheHouse = ServiceLocator.get<ThirdLgOnTheHouse>()
    private val monthlyLimit = ServiceLocator.get<MonthlyLimit>()

    fun create(courier: Courier): Rule = when (courier) {
        Courier.LP -> lpRules
        Courier.MR -> mrRules
    }

    private val lpRules by lazy {
        chain(
            smMonthlyLimit,
            thirdLgOnTheHouse,
            monthlyLimit,
        )
    }

    private val mrRules by lazy {
        chain(
            smMonthlyLimit,
            monthlyLimit,
        )
    }

    private fun chain(vararg rules: Rule): Rule {
        val firstRule = rules[0]
        var currRule: Rule? = null

        for (rule in rules) {
            currRule?.nextRule = rule
            currRule = rule
        }

        return firstRule
    }

}



