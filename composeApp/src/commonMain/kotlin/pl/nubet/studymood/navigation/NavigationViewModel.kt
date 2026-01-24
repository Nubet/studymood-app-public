package pl.nubet.studymood.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.nubet.studymood.core.logging.Log
import pl.nubet.studymood.data.repository.OnboardingRepository

class NavigationViewModel(private val onboardingRepository: OnboardingRepository) : ViewModel() {

    private val _currentDestination = MutableStateFlow<AppDestination>(AppDestination.Splash)
    val currentDestination = _currentDestination.asStateFlow()

    private val _overlays = MutableStateFlow<List<AppDestination.Overlay>>(emptyList())
    val overlays = _overlays.asStateFlow()

    init {
        checkOnboardingStatus()
    }

    private fun checkOnboardingStatus() {
        viewModelScope.launch {
            try {
                val isCompleted = onboardingRepository.isOnboardingCompleted()
                _currentDestination.value =
                    if (isCompleted) {
                        Log.d("Onboarding completed, navigating to Main", tag = "Navigation")
                        AppDestination.Main()
                    } else {
                        Log.d("Onboarding not completed, showing Onboarding", tag = "Navigation")
                        AppDestination.Onboarding
                    }
            } catch (e: Exception) {
                Log.e("Error checking onboarding status: ${e.message}", e, tag = "Navigation")
                _currentDestination.value = AppDestination.Onboarding
            }
        }
    }

    fun navigate(destination: AppDestination) {
        when (destination) {
            is AppDestination.Overlay -> {
                Log.d("Adding overlay: $destination", tag = "Navigation")
                _overlays.value = _overlays.value + destination
            }
            else -> {
                Log.d("Navigating to: $destination", tag = "Navigation")
                _currentDestination.value = destination
                _overlays.value = emptyList()
            }
        }
    }

    fun popOverlay() {
        if (_overlays.value.isNotEmpty()) {
            Log.d("Popping overlay", tag = "Navigation")
            _overlays.value = _overlays.value.dropLast(1)
        }
    }

    fun selectTab(tab: Tab) {
        Log.d("Selecting tab: $tab", tag = "Navigation")
        _currentDestination.value = AppDestination.Main(tab)
    }

    fun resetOnboarding() {
        Log.d("Resetting to onboarding", tag = "Navigation")
        _currentDestination.value = AppDestination.Onboarding
        _overlays.value = emptyList()
    }
}
