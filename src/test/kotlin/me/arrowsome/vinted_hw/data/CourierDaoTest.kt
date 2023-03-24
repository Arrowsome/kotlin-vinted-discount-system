package me.arrowsome.vinted_hw.data

import me.arrowsome.vinted_hw.model.Courier
import me.arrowsome.vinted_hw.model.Size
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CourierDaoTest {
    private lateinit var courierDao: CourierDao

    @BeforeEach
    fun setup() {
        courierDao = CourierDao()
    }

    @Test
    fun `finding a fee based on courier and size of shipment returns a positive number`() {
        val fee = courierDao.findFee(Courier.LP, Size.SM)

        assertEquals(1.5f, fee)
    }

    @Test
    fun `find lowest fee based on size returns a positive number`() {
        val fee = courierDao.findLowestFee(Size.LG)

        assertEquals(4.00f, fee)
    }

}