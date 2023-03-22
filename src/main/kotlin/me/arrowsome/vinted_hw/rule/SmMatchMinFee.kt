package me.arrowsome.vinted_hw.rule

import me.arrowsome.vinted_hw.data.CourierDao
import me.arrowsome.vinted_hw.model.Shipment
import me.arrowsome.vinted_hw.model.Size

class SmMatchMinFee(private val providerDao: CourierDao) : BaseRule() {

    override fun applyDiscount(shipment: Shipment): Shipment {
        return if (shipment.size == Size.SM)
            calcDiscount(shipment)
        else
            shipment
    }

    private fun calcDiscount(shipment: Shipment): Shipment {
        val minFee = providerDao.findLowestFee(shipment.size)

        val discount = shipment.fee.base - minFee
        val newShipment = shipment.copy(
            fee = shipment.fee.copy(
                discount = discount,
            )
        )

        return nextRule?.applyDiscount(newShipment)
            ?: newShipment
    }

}