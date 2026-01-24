package pl.nubet.studymood.data.db

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory() {
    fun createDriver(dbName: String = "studymood.db", context: Any? = null): SqlDriver
}
