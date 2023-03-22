package me.arrowsome.vinted_hw.data

import me.arrowsome.vinted_hw.model.Courier
import me.arrowsome.vinted_hw.model.Size

class CourierDao {

    fun findFee(courier: Courier, size: Size): Float {
        return courierTable
            .find { it.courier ==  courier && it.size == size }
            ?.fee ?: -1f
    }

    fun findLowestFee(size: Size): Float {
        return courierTable
            .filter { it.size == size }
            .minOf { it.fee }
    }

}