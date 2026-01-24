package pl.nubet.studymood

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pl.nubet.studymood.data.db.DriverFactory
import pl.nubet.studymood.di.initKoin

var appContext: Context? = null

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        appContext = applicationContext

        val driver = DriverFactory().createDriver(context = this)
        initKoin(driver)

        setContent { App() }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
