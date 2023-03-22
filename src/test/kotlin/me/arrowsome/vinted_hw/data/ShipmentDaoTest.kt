package me.arrowsome.vinted_hw.data

import me.arrowsome.vinted_hw.model.Courier
import me.arrowsome.vinted_hw.model.Fee
import me.arrowsome.vinted_hw.model.Shipment
import me.arrowsome.vinted_hw.model.Size
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class ShipmentDaoTest {
    private lateinit var shipmentDao: ShipmentDao

    @BeforeEach
    fun setup() {
        shipmentDao = ShipmentDao()

        shipmentDao.insertShipment(
            SHIPMENT_SM.date.monthValue,
            SHIPMENT_SM.fee.discount,
            SHIPMENT_SM.courier,
            SHIPMENT_SM.size,
        )

        shipmentDao.insertShipment(
            SHIPMENT_MD.date.monthValue,
            SHIPMENT_MD.fee.discount,
            SHIPMENT_MD.courier,
            SHIPMENT_MD.size,
        )
    }

    @AfterEach
    fun cleanup() {
        clearInMemoryData()
    }

    @Test
    fun `discount sum is calculated by month`() {
        val sum = shipmentDao.getDiscountSumByMonth(SHIPMENT_SM.date.monthValue)

        assertEquals(2f, sum)
    }

    @Test
    fun `discount count in a month is calculated`() {
        val count = shipmentDao.getDiscountCountInMonthBySizeAndCourier(
            SHIPMENT_SM.date.monthValue,
            SHIPMENT_SM.size,
            SHIPMENT_SM.courier,
        )

        assertEquals(1, count)
    }

    companion object {
        private val SHIPMENT_SM = Shipment(
            size = Size.SM,
            courier = Courier.LP,
            date = LocalDate.parse("2023-07-29"),
            fee = Fee(base = 2f, discount = 0.5f)
        )

        private val SHIPMENT_MD = Shipment(
            size = Size.MD,
            courier = Courier.LP,
            date = LocalDate.parse("2023-07-29"),
            fee = Fee(base = 2f, discount = 1.5f)
        )
    }

}