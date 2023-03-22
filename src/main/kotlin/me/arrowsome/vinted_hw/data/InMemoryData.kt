package me.arrowsome.vinted_hw.data

import me.arrowsome.vinted_hw.model.Courier
import me.arrowsome.vinted_hw.model.Courier.LP
import me.arrowsome.vinted_hw.model.Courier.MR
import me.arrowsome.vinted_hw.model.Size
import me.arrowsome.vinted_hw.model.Size.SM
import me.arrowsome.vinted_hw.model.Size.MD
import me.arrowsome.vinted_hw.model.Size.LG

internal data class CourierRow(
    val courier: Courier,
    val size: Size,
    val fee: Float,
)

internal val courierTable = listOf(
    CourierRow(LP, SM, 1.50f),
    CourierRow(LP, MD, 4.90f),
    CourierRow(LP, LG, 6.90f),
    CourierRow(MR, SM, 2.00f),
    CourierRow(MR, MD, 3.00f),
    CourierRow(MR, LG, 4.00f),
)

internal data class ShipmentRow(
    val month: Int,
    val size: Size,
    val courier: Courier,
    val discount: Float,
)

internal val shipmentTable = mutableListOf<ShipmentRow>()

fun clearInMemoryData() {
    shipmentTable.clear()
}