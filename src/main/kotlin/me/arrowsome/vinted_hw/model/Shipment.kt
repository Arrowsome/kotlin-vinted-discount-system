package me.arrowsome.vinted_hw.model

import java.time.LocalDate

data class Shipment(
    val size: Size,
    val courier: Courier,
    val date: LocalDate,
    val fee: Fee
)
