package me.arrowsome.vinted_hw.spec

import me.arrowsome.vinted_hw.Pack

interface SizeRule {
    fun applySmRule(pack: Pack): Discount
    fun applyMdRule(pack: Pack): Discount
    fun applyLgRule(pack: Pack): Discount
}