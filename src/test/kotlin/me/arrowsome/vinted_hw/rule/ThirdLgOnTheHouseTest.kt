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

class ThirdLgOnTheHouseTest {
    private lateinit var thirdLgOnTheHouse: ThirdLgOnTheHouse
    private lateinit var shipmentDao: ShipmentDao

    @BeforeEach
    fun setup() {
        shipmentDao = mockk()
        thirdLgOnTheHouse = ThirdLgOnTheHouse(shipmentDao)
    }

    @Test
    fun `the third large pack shipping is discounted in a month`() {
        every {
            shipmentDao.getDiscountCountInMonthBySize(SHIPMENT_LG.date.monthValue, SHIPMENT_LG.size)
        } returns 3

        val shipment = thirdLgOnTheHouse.applyDiscount(SHIPMENT_LG)

        verify {
            shipmentDao.getDiscountCountInMonthBySize(SHIPMENT_LG.date.monthValue, SHIPMENT_LG.size)
        }
        assertEquals(DiscountStatus.APPLIED, shipment.fee.discountStatus)
        assertEquals(SHIPMENT_LG.fee.base, shipment.fee.discount)
    }

    @Test
    fun `non third large pack has non applicable discount status`() {
        every {
            shipmentDao.getDiscountCountInMonthBySize(SHIPMENT_LG.date.monthValue, SHIPMENT_LG.size)
        } returns 2

        val shipment = thirdLgOnTheHouse.applyDiscount(SHIPMENT_LG)

        verify {
            shipmentDao.getDiscountCountInMonthBySize(SHIPMENT_LG.date.monthValue, SHIPMENT_LG.size)
        }
        assertEquals(DiscountStatus.NON_APPLICABLE, shipment.fee.discountStatus)
        assertEquals(0f, shipment.fee.discount)
    }

    @Test
    fun `non large pack has non applicable discount status`() {
        every {
            shipmentDao.getDiscountCountInMonthBySize(SHIPMENT_MD.date.monthValue, SHIPMENT_MD.size)
        } returns 3

        val shipment = thirdLgOnTheHouse.applyDiscount(SHIPMENT_MD)

        verify {
            shipmentDao.getDiscountCountInMonthBySize(SHIPMENT_MD.date.monthValue, SHIPMENT_MD.size)
        }
        assertEquals(DiscountStatus.NON_APPLICABLE, shipment.fee.discountStatus)
        assertEquals(0f, shipment.fee.discount)
    }

    companion object {
        private val SHIPMENT_LG = Shipment(
            size = Size.LG,
            courier = Courier.LP,
            date = LocalDate.parse("2023-07-29"),
            fee = Fee(base = 2.5f, discount = 1.5f)
        )

        private val SHIPMENT_MD = Shipment(
            size = Size.MD,
            courier = Courier.LP,
            date = LocalDate.parse("2023-07-29"),
            fee = Fee(base = 2.5f, discount = 1.5f)
        )
    }
}