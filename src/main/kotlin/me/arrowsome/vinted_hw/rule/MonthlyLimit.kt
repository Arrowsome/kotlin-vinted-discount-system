package me.arrowsome.vinted_hw.rule

import me.arrowsome.vinted_hw.data.ShipmentDao
import me.arrowsome.vinted_hw.model.Shipment

class MonthlyLimit(private val shipmentDao: ShipmentDao) : BaseRule() {

    override fun applyDiscount(shipment: Shipment): Shipment {
        val sumOfMonthDis = shipmentDao.getDiscountSumByMonth(shipment.date.monthValue)

        val newShipment = if (sumOfMonthDis >= 10f)
            shipment.copy(fee = shipment.fee.copy(discount = 0f))
        else if ((sumOfMonthDis + shipment.fee.discount) > 10f)
            shipment.copy(
                fee = shipment.fee.copy(
                    discount = shipment.fee.discount - ((sumOfMonthDis + shipment.fee.discount) - 10f)
                )
            )
        else
            shipment

        return nextRule?.applyDiscount(newShipment)
            ?: newShipment

    }

}