package pl.nubet.studymood.di

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin(driver: SqlDriver) {
    startKoin {
        val driverModule = module { single { driver } }
        modules(appModule, driverModule)
    }
}
