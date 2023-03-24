package me.arrowsome.vinted_hw.rule

import me.arrowsome.vinted_hw.data.CourierDao
import me.arrowsome.vinted_hw.data.ShipmentDao
import me.arrowsome.vinted_hw.model.Courier
import me.arrowsome.vinted_hw.model.Shipment

interface Rule {
    var nextRule: Rule?
    fun applyDiscount(shipment: Shipment): Shipment
}

abstract class BaseRule : Rule {
    override var nextRule: Rule? = null
}

class RuleFactory {
    private val courierDao = CourierDao()
    private val shipmentDao = ShipmentDao()

    fun create(courier: Courier): Rule = when (courier) {
        Courier.LP -> lpRules
        Courier.MR -> mrRules
    }

    private val lpRules by lazy {
//        SmMatchMinFee(courierDao)
//            .then(ThirdLgOnTheHouse(shipmentDao)
//                .then(MonthlyLimit(shipmentDao)))
        chain(
            SmMatchMinFee(courierDao),
            ThirdLgOnTheHouse(shipmentDao),
            MonthlyLimit(shipmentDao),
        )
    }

    private val mrRules by lazy {
//        SmMatchMinFee(courierDao)
//            .then(MonthlyLimit(shipmentDao))
        chain(
            SmMatchMinFee(courierDao),
            MonthlyLimit(shipmentDao),
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



