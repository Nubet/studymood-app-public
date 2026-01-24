package pl.nubet.studymood.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.nubet.studymood.core.logging.Log
import pl.nubet.studymood.data.debug.DebugDataSeeder
import pl.nubet.studymood.data.repository.OnboardingRepository
import pl.nubet.studymood.domain.model.ThemePreference

sealed interface AppInitState {
    data object Initializing : AppInitState

    data class Ready(val themePreference: ThemePreference) : AppInitState

    data class Error(val message: String) : AppInitState
}

class AppInitViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val debugDataSeeder: DebugDataSeeder,
) : ViewModel() {

    private val _initState = MutableStateFlow<AppInitState>(AppInitState.Initializing)
    val initState = _initState.asStateFlow()

    val themePreference =
        onboardingRepository
            .observeThemePreference()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ThemePreference.LIGHT,
            )

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch {
            try {
                Log.d("Starting app initialization", tag = "AppInit")

                val currentTheme = onboardingRepository.getThemePreference()
                onboardingRepository.setThemePreference(currentTheme)

                Log.d("Theme loaded: $currentTheme", tag = "AppInit")

                debugDataSeeder.seedIfNeeded()

                _initState.value = AppInitState.Ready(currentTheme)
                Log.i("App initialization complete", tag = "AppInit")
            } catch (e: Exception) {
                Log.e("App initialization failed: ${e.message}", e, tag = "AppInit")
                _initState.value = AppInitState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
