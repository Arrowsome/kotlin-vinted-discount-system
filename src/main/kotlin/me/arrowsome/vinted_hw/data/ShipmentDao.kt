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
            .fold(0f) { acc, row -> acc + row.discount }
    }

    fun getDiscountCountInMonthBySizeAndCourier(month: Int, size: Size, courier: Courier): Int  {
        return shipmentTable
            .count { it.month == month && it.size == size && it.courier == courier }
    }

}