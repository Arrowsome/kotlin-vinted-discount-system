package me.arrowsome.vinted_hw.rule

import me.arrowsome.vinted_hw.model.Shipment

interface Rule {
    var nextRule: Rule?
    fun applyDiscount(shipment: Shipment): Shipment
}

abstract class BaseRule : Rule {
    override var nextRule: Rule? = null
}

fun Rule.then(rule: Rule): Rule {
    this.nextRule = rule
    return rule
}





