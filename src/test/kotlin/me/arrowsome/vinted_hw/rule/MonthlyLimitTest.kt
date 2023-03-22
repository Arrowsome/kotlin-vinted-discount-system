package me.arrowsome.vinted_hw.rule

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.arrowsome.vinted_hw.data.ShipmentDao
import me.arrowsome.vinted_hw.model.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class MonthlyLimitTest {
    private lateinit var monthlyLimit: MonthlyLimit
    private lateinit var shipmentDao: ShipmentDao

    @BeforeEach
    fun setup() {
        shipmentDao = mockk()
        monthlyLimit = MonthlyLimit(shipmentDao)
    }

    @Test
    fun `discount exceeding 10 euro is non applicable`() {
        every { shipmentDao.getDiscountSumByMonth(SHIPMENT.date.monthValue) } returns 10f

        val shipment = monthlyLimit.applyDiscount(SHIPMENT)

        verify { shipmentDao.getDiscountSumByMonth(SHIPMENT.date.monthValue) }
        assertEquals(0f, shipment.fee.discount)
        assertEquals(DiscountStatus.NON_APPLICABLE, shipment.fee.discountStatus)
    }

    @Test
    fun `discount is reduced to match 10 euro monthly limit`() {
        every { shipmentDao.getDiscountSumByMonth(SHIPMENT.date.monthValue) } returns 9.5f

        val shipment = monthlyLimit.applyDiscount(SHIPMENT)

        assertEquals(0.5f, shipment.fee.discount)
        verify { shipmentDao.getDiscountSumByMonth(SHIPMENT.date.monthValue) }
        assertEquals(DiscountStatus.APPLIED, shipment.fee.discountStatus)
    }

    companion object {
        private val SHIPMENT = Shipment(
            size = Size.SM,
            courier = Courier.LP,
            date = LocalDate.parse("2023-07-29"),
            fee = Fee(base = 2.5f, discount = 1.5f)
        )
    }

}