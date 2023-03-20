package me.arrowsome.vinted_hw

import me.arrowsome.vinted_hw.spec.Discount
import java.time.LocalDate

val PACK_SM_MR_20231107 = Pack(
    size = PackSize.SM,
    provider = PackProvider.MR,
    date = LocalDate.parse("2023-11-07")
)

val DISCOUNT_PACK_SM_MR_20231107 = Discount.Applied(
    discountAmount = 0.5f,
    basePrice = 2.0f,
)

val PACK_SM_UNKNOWN_20231107 = Pack(
    size = PackSize.SM,
    provider = PackProvider.UNKNOWN,
    date = LocalDate.parse("2023-11-07")
)

val PACK_MD_LP_20231107 = Pack(
    size = PackSize.MD,
    provider = PackProvider.LP,
    date = LocalDate.parse("2023-11-07")
)

val PACK_LG_LP_20231107 = Pack(
    size = PackSize.LG,
    provider = PackProvider.LP,
    date = LocalDate.parse("2023-11-07")
)