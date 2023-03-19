package me.arrowsome.vinted_hw

import java.time.LocalDate

data class Pack(
    val size: PackSize,
    val provider: PackProvider,
    val date: LocalDate,
)
