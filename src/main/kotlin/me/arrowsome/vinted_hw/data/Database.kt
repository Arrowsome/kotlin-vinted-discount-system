package me.arrowsome.vinted_hw.data

class Database private constructor() {

    companion object {
        @Volatile
        private var instance: Database? = null

        fun createDatabase(): Database {
            return instance ?: synchronized(this) {
                instance ?: Database().also { instance = it }
            }
        }
    }

    fun shipmentDao() = ShipmentDao()
    fun providerDao() = CourierDao()
}