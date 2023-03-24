package me.arrowsome.vinted_hw.parser

import me.arrowsome.vinted_hw.data.CourierDao
import me.arrowsome.vinted_hw.data.ShipmentDao
import org.approvaltests.Approvals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.File

class ShipmentFileProcessorTest {
    private lateinit var shipmentFileProcessor: ShipmentFileProcessor
    private lateinit var byteArrayOutputStream: ByteArrayOutputStream

    @BeforeEach
    fun setup() {
        byteArrayOutputStream = ByteArrayOutputStream()

        shipmentFileProcessor = ShipmentFileProcessor(
            FILE_READER_DEST.inputStream(),
            byteArrayOutputStream,
            CourierDao(),
            ShipmentDao(),
        )
    }

    @AfterEach
    fun cleanup() {
        byteArrayOutputStream.close()
    }

    @Test
    fun `discount file generated`() {
        shipmentFileProcessor.process()

        Approvals.verify(byteArrayOutputStream.toString())
    }

    companion object {
        val FILE_READER_DEST = File(ShipmentFileProcessorTest::class.java
            .classLoader
            .getResource("shipments.txt")!!
            .file,
        )
    }

}