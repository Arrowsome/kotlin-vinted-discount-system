package me.arrowsome.vinted_hw.rule

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.arrowsome.vinted_hw.data.ProviderDao
import me.arrowsome.vinted_hw.model.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class SmMatchMinFeeTest {
    private lateinit var providerDao: ProviderDao
    private lateinit var smMatchMinFee: SmMatchMinFee

    @BeforeEach
    fun setup() {
        providerDao = mockk()
        smMatchMinFee = SmMatchMinFee(providerDao)
    }

    @Test
    fun `small shipment discount is applied`() {
        every { providerDao.findLowestFee(SHIPMENT_SM.size) } returns 2f

        val discount = smMatchMinFee.applyDiscount(SHIPMENT_SM)

        verify { providerDao.findLowestFee(SHIPMENT_SM.size) }
        assertEquals(DiscountStatus.APPLIED, discount.fee.discountStatus)
        assertEquals(0.5f, discount.fee.discount)
    }

    @Test
    fun `non small shipment discount is non applicable`() {
        val discount = smMatchMinFee.applyDiscount(SHIPMENT_MD)

        assertEquals(DiscountStatus.NON_APPLICABLE, discount.fee.discountStatus)
        assertEquals(0f, discount.fee.discount)
    }

    companion object {
        private val SHIPMENT_SM = Shipment(
            size = Size.SM,
            courier = Courier.LP,
            date = LocalDate.parse("2023-07-29"),
            fee = Fee(base = 2.5f, discount = 0f)
        )

        private val SHIPMENT_MD = Shipment(
            size = Size.MD,
            courier = Courier.LP,
            date = LocalDate.parse("2023-07-29"),
            fee = Fee(base = 2.5f, discount = 0f)
        )
    }
}