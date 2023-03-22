package me.arrowsome.vinted_hw.rule

import me.arrowsome.vinted_hw.data.ShipmentDao
import me.arrowsome.vinted_hw.model.Shipment
import me.arrowsome.vinted_hw.model.Size

class ThirdLgOnTheHouse(private val shipmentDao: ShipmentDao) : BaseRule() {

    override fun applyDiscount(shipment: Shipment): Shipment {
        val newShipment = if (shipment.size == Size.LG)
            calcDiscount(shipment)
        else
            shipment


        return nextRule?.applyDiscount(newShipment)
            ?: newShipment
    }

    private fun calcDiscount(shipment: Shipment): Shipment {
        val count = shipmentDao.getDiscountCountInMonthBySizeAndCourier(
            shipment.date.monthValue,
            shipment.size,
            shipment.courier,
        )

        val discount = if (count == 3) shipment.fee.base else 0f

        return shipment.copy(
            fee = shipment.fee.copy(
                discount = discount,
            )
        )
    }

}