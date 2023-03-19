package me.arrowsome.vinted_hw

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.arrowsome.vinted_hw.data.DiscountDao
import me.arrowsome.vinted_hw.data.ProviderDao
import me.arrowsome.vinted_hw.spec.LpDiscountSpec
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LpDiscountSpecTests {
    private lateinit var providerDao: ProviderDao
    private lateinit var discountDao: DiscountDao
    private lateinit var lpDiscountSpec: LpDiscountSpec

    @BeforeEach
    fun setup() {
        providerDao = mockk()
        discountDao = mockk()
        lpDiscountSpec = LpDiscountSpec(providerDao, discountDao)
    }

    @Test
    fun `small package discount is applied`() {
        every {
            providerDao.findFee(
                provider = PACK_SM_MR_20231107.provider,
                size = PACK_SM_MR_20231107.size
            )
        } returns 2.0f

        every {
            providerDao.findLowestFee(size = PACK_SM_MR_20231107.size)
        } returns 1.50f

        every {
            discountDao.getDiscountSumByMonth(month = PACK_SM_MR_20231107.date.monthValue)
        } returns 0.0f

        val result = lpDiscountSpec.calculateDiscount(PACK_SM_MR_20231107)

        verify {
            providerDao.findFee(
                provider = PACK_SM_MR_20231107.provider,
                size = PACK_SM_MR_20231107.size
            )
        }

        verify {
            providerDao.findLowestFee(size = PackSize.SM)
        }

        assertEquals(Result.Success(PACK_SM_MR_20231107_DISCOUNTED), result)
    }

    @Test
    fun `non existing provider is ignored`() {
        every {
            providerDao.findFee(
                provider = PACK_SM_UNKNOWN_20231107.provider,
                size = PACK_SM_UNKNOWN_20231107.size
            )
        } returns null


        val result = lpDiscountSpec.calculateDiscount(PACK_SM_UNKNOWN_20231107)


        verify {
            providerDao.findFee(
                provider = PACK_SM_UNKNOWN_20231107.provider,
                size = PACK_SM_UNKNOWN_20231107.size
            )
        }

        assertEquals(Result.Failure(NoDiscountStatus.IGNORED), result)
    }

    @Test
    fun `discount exceeding 10 euro is non applicable`() {
        every {
            providerDao.findFee(
                provider = PACK_SM_MR_20231107.provider,
                size = PACK_SM_MR_20231107.size
            )
        } returns 2.0f

        every {
            providerDao.findLowestFee(size = PACK_SM_MR_20231107.size)
        } returns 1.50f

        every {
            discountDao.getDiscountSumByMonth(PACK_SM_MR_20231107.date.monthValue)
        } returns 10.00f

        val result = lpDiscountSpec.calculateDiscount(PACK_SM_MR_20231107)

        verify {
            providerDao.findFee(
                provider = PACK_SM_MR_20231107.provider,
                size = PACK_SM_MR_20231107.size,
            )
        }

        verify {
            providerDao.findLowestFee(size = PackSize.SM)
        }

        verify {
            discountDao.getDiscountSumByMonth(PACK_SM_MR_20231107.date.monthValue)
        }

        assertEquals(Result.Failure(NoDiscountStatus.NON_APPLICABLE), result)
    }

    @Test
    fun `the discount is reduced to match 10 euro monthly limit`() {
        every {
            providerDao.findFee(
                provider = PACK_SM_MR_20231107.provider,
                size = PACK_SM_MR_20231107.size,
            )
        } returns 2.0f

        every {
            providerDao.findLowestFee(size = PACK_SM_MR_20231107.size)
        } returns 1.50f

        every {
            discountDao.getDiscountSumByMonth(month = PACK_SM_MR_20231107.date.monthValue)
        } returns 9.90f

        val result = lpDiscountSpec.calculateDiscount(pack = PACK_SM_MR_20231107)

        verify {
            providerDao.findFee(
                provider = PACK_SM_MR_20231107.provider,
                size = PACK_SM_MR_20231107.size,
            )
        }

        verify {
            providerDao.findLowestFee(size = PackSize.SM)
        }

        verify {
            discountDao.getDiscountSumByMonth(month = PACK_SM_MR_20231107.date.monthValue)
        }

        assertTrue(result is Result.Success<DiscountedPack>)
        assertEquals(result.data.pack, PACK_SM_MR_20231107)
        assertEquals(result.data.discountAmount, 0.1f, 0.01f)
        assertEquals(result.data.newPrice, 1.9f, 0.01f)
    }

    @Test
    fun `medium size packs are always ignored`() {
        val result = lpDiscountSpec.calculateDiscount(PACK_MD_LP_20231107)

        assertTrue(result is Result.Failure<NoDiscountStatus>)
        assertEquals(NoDiscountStatus.IGNORED, result.error)
    }

    @Test
    fun `the third large pack shipping is discounted in a month`() {
        every {
            providerDao.findFee(
                provider = PACK_LG_LP_20231107.provider,
                size = PACK_LG_LP_20231107.size,
            )
        } returns 6.90f

        every {
            discountDao.getDiscountCountInMonthBySize(
                month = PACK_LG_LP_20231107.date.monthValue,
                size = PACK_LG_LP_20231107.size,
            )
        } returns 3

        every {
            discountDao.getDiscountSumByMonth(PACK_LG_LP_20231107.date.monthValue)
        } returns 0f

        val result = lpDiscountSpec.calculateDiscount(PACK_LG_LP_20231107)

        verify {
            providerDao.findFee(
                provider = PACK_LG_LP_20231107.provider,
                size = PACK_LG_LP_20231107.size,
            )
        }

        verify {
            discountDao.getDiscountCountInMonthBySize(
                month = PACK_LG_LP_20231107.date.monthValue,
                size = PACK_LG_LP_20231107.size,
            )
        }

        assertTrue(result is Result.Success<DiscountedPack>)
        assertEquals(PACK_LG_LP_20231107, result.data.pack)
        assertEquals(6.90f, result.data.discountAmount)
        assertEquals(0.0f, result.data.newPrice)
    }

    @Test
    fun `non 3rd lg packs are not free of charge`() {
        every {
            providerDao.findFee(
                provider = PACK_LG_LP_20231107.provider,
                size = PACK_LG_LP_20231107.size,
            )
        } returns 6.90f

        every {
            discountDao.getDiscountCountInMonthBySize(
                month = PACK_LG_LP_20231107.date.monthValue,
                size = PACK_LG_LP_20231107.size,
            )
        } returns 4

        every {
            discountDao.getDiscountSumByMonth(PACK_LG_LP_20231107.date.monthValue)
        } returns 0f

        val result = lpDiscountSpec.calculateDiscount(PACK_LG_LP_20231107)

        verify {
            providerDao.findFee(
                provider = PACK_LG_LP_20231107.provider,
                size = PACK_LG_LP_20231107.size,
            )
        }

        verify {
            discountDao.getDiscountCountInMonthBySize(
                month = PACK_LG_LP_20231107.date.monthValue,
                size = PACK_LG_LP_20231107.size,
            )
        }

        assertTrue(result is Result.Success<DiscountedPack>)
        assertEquals(PACK_LG_LP_20231107, result.data.pack)
        assertEquals(0.0f, result.data.discountAmount)
        assertEquals(6.90f, result.data.newPrice)
    }

    @Test
    fun `discount of lg pack should not exceed 10 euro`() {
        every {
            providerDao.findFee(
                provider = PACK_LG_LP_20231107.provider,
                size = PACK_LG_LP_20231107.size,
            )
        } returns 6.90f

        every {
            discountDao.getDiscountCountInMonthBySize(
                month = PACK_LG_LP_20231107.date.monthValue,
                size = PACK_LG_LP_20231107.size,
            )
        } returns 3

        every {
            discountDao.getDiscountSumByMonth(PACK_LG_LP_20231107.date.monthValue)
        } returns 8.90f

        val result = lpDiscountSpec.calculateDiscount(PACK_LG_LP_20231107)

        verify {
            providerDao.findFee(
                provider = PACK_LG_LP_20231107.provider,
                size = PACK_LG_LP_20231107.size,
            )
        }

        verify {
            discountDao.getDiscountCountInMonthBySize(
                month = PACK_LG_LP_20231107.date.monthValue,
                size = PACK_LG_LP_20231107.size,
            )
        }

        assertTrue(result is Result.Success<DiscountedPack>)
        assertEquals(PACK_LG_LP_20231107, result.data.pack)
        assertEquals(1.1f, result.data.discountAmount, 0.01f)
        assertEquals(5.80f, result.data.newPrice, 0.01f)
    }

}