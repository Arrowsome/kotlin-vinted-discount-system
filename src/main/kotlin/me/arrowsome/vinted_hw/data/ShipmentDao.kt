package me.arrowsome.vinted_hw.data

import me.arrowsome.vinted_hw.model.Courier
import me.arrowsome.vinted_hw.model.Size

class ShipmentDao {

    fun insertShipment(month: Int, discount: Float, courier: Courier, size: Size) {
        shipmentTable.add(
            ShipmentRow(
                month,
                size,
                courier,
                discount,
            )
        )
    }

    fun getDiscountSumByMonth(month: Int): Float {
        return shipmentTable
            .filter { it.month == month }
            .sumOf { it.discount.toDouble() }.toFloat()
    }

    fun getDiscountCountInMonthBySizeAndCourier(month: Int, size: Size, courier: Courier): Int  {
        return shipmentTable
            .count { it.month == month && it.size == size && it.courier == courier }
    }

}