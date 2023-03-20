package me.arrowsome.vinted_hw.spec

fun interface BudgetRule {
    fun applyBudgetRule(discount: Discount): Discount
}