package me.arrowsome.vinted_hw.parser

import me.arrowsome.vinted_hw.data.CourierDao
import me.arrowsome.vinted_hw.data.ShipmentDao
import me.arrowsome.vinted_hw.model.*
import me.arrowsome.vinted_hw.rule.RuleFactory
import java.io.InputStream
import java.io.OutputStream
import java.time.LocalDate
import java.util.*

class ShipmentFileProcessor(
    inputStream: InputStream,
    outputStream: OutputStream,
    private val courierDao: CourierDao,
    private val shipmentDao: ShipmentDao,
) : ShipmentProcessor(inputStream, outputStream) {
    private val reader = inputStream
        .bufferedReader()

    private val writer = outputStream
        .bufferedWriter()

    override fun process() {
        reader
            .readLines()
            .map { it.split("\\s".toRegex()) }
            .forEach { strings ->
                try {
                    val shipment = parse(strings)
                    val discountStr =
                        if (shipment.fee.discount == 0f)
                            "-"
                        else
                            String.format("%.2f", shipment.fee.discount)
                    val payableStr = String.format("%.2f", shipment.fee.payable)
                    writer
                        .append(strings.joinToString(" "))
                        .append(' ')
                        .append(payableStr)
                        .append(' ')
                        .append(discountStr)
                        .append('\n')

                    shipmentDao.insertShipment(
                        month = shipment.date.monthValue,
                        discount = shipment.fee.discount,
                        courier = shipment.courier,
                        size = shipment.size,
                    )
                } catch (exc: Exception) {
                    writer
                        .append(strings.joinToString(" "))
                        .append(' ')
                        .append("Ignored")
                        .append('\n')
                } finally {
                    writer.flush()
                }
            }
    }

    private fun parse(entry: List<String>): Shipment {
        if (entry.count() != 3)
            throw ParseException()

        val (date, size, courier) = entry

        val sizeObj = when (size.uppercase(Locale.ENGLISH)) {
            "S" -> Size.SM
            "M" -> Size.MD
            "L" -> Size.LG
            else -> throw ParseException()
        }

        val courierObj = when (courier.uppercase(Locale.ENGLISH)) {
            "LP" -> Courier.LP
            "MR" -> Courier.MR
            else -> throw ParseException()
        }

        val dateObj = try {
            LocalDate.parse(date)
        } catch (exc: Exception) {
            throw ParseException()
        }

        val baseFee = courierDao.findFee(courierObj, sizeObj)
        val feeObj = Fee(base = baseFee)

        val shipmentObj = Shipment(
            size = sizeObj,
            courier = courierObj,
            date = dateObj,
            fee = feeObj
        )

        return RuleFactory
            .create(courierObj)
            .applyDiscount(shipmentObj)
    }
}