package pl.nubet.studymood

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.koinInject
import pl.nubet.studymood.app.AppInitViewModel
import pl.nubet.studymood.app.AppRoot
import pl.nubet.studymood.ui.theme.AppTheme

@Composable
fun App() {
    val appInitViewModel: AppInitViewModel = koinInject()
    val themePreference by appInitViewModel.themePreference.collectAsState()

    AppTheme(dark = themePreference == pl.nubet.studymood.domain.model.ThemePreference.DARK) {
        AppRoot()
    }
}
