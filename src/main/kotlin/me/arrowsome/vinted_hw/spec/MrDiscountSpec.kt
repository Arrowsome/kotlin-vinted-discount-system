package me.arrowsome.vinted_hw.spec

import me.arrowsome.vinted_hw.data.DiscountDao
import me.arrowsome.vinted_hw.data.ProviderDao

class MrDiscountSpec(
    providerDao: ProviderDao,
    discountDao: DiscountDao,
) : DefaultDiscountSpec(providerDao, discountDao)