package pl.nubet.studymood.data.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DriverFactory {
    actual fun createDriver(dbName: String, context: Any?): SqlDriver {
        val ctx =
            context as? Context
                ?: error(
                    "Android Context required. Pass 'context = this' from an Activity/Application"
                )

        return AndroidSqliteDriver(
            schema = StudyMoodDatabase.Companion.Schema,
            context = ctx,
            name = dbName,
            callback = AndroidSqliteDriver.Callback(StudyMoodDatabase.Companion.Schema),
        )
    }
}
