package me.arrowsome.vinted_hw

import me.arrowsome.vinted_hw.di.ServiceLocator
import me.arrowsome.vinted_hw.parser.ShipmentFileProcessor
import java.io.ByteArrayOutputStream

class App {

    fun run() {
        val inputStream = App::class.java.classLoader.getResource("inputs.txt")!!.openStream()
        val outputStream = ByteArrayOutputStream()

        val processor = ShipmentFileProcessor(
            inputStream,
            outputStream,
            ServiceLocator.get(),
            ServiceLocator.get(),
        )

        processor.process()
        println(outputStream.toString().trim())
        outputStream.close()
    }

}