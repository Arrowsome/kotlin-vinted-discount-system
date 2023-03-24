package me.arrowsome.vinted_hw.parser

import java.io.InputStream
import java.io.OutputStream

abstract class ShipmentProcessor(
    protected val inputStream: InputStream,
    protected val outputStream: OutputStream
) : Processor