package me.arrowsome.vinted_hw.data

import me.arrowsome.vinted_hw.PackProvider
import me.arrowsome.vinted_hw.PackSize

class ProviderDao {

    fun findFee(provider: PackProvider, size: PackSize): Float? = TODO()

    fun findLowestFee(size: PackSize): Float? = TODO()

}