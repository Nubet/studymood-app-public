package pl.nubet.studymood

import androidx.compose.ui.window.ComposeUIViewController
import pl.nubet.studymood.data.db.DriverFactory
import pl.nubet.studymood.di.initKoin

fun MainViewController() = ComposeUIViewController {
    val driver = DriverFactory().createDriver()
    initKoin(driver)
    App()
}
