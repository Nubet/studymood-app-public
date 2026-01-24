package pl.nubet.studymood.data.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DriverFactory {
    actual fun createDriver(dbName: String, context: Any?): SqlDriver {
        return NativeSqliteDriver(
            schema = StudyMoodDatabase.Companion.Schema,
            name = dbName,
            onConfiguration = { config ->
                config.copy(extendedConfig = config.extendedConfig.copy(basePath = null))
            },
        )
    }
}
