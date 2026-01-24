package pl.nubet.studymood.presentation.onboarding

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.nubet.studymood.core.util.MainScopeProvider
import pl.nubet.studymood.data.repository.OnboardingRepository
import pl.nubet.studymood.domain.model.FocusArea
import pl.nubet.studymood.domain.model.OnboardingData
import pl.nubet.studymood.domain.model.ReminderTime

class OnboardingViewModel(
    private val repository: OnboardingRepository,
    private val scopeProvider: MainScopeProvider,
) {
    private val scope = scopeProvider.scope

    private val _state = MutableStateFlow(OnboardingUiState())
    val state: StateFlow<OnboardingUiState> = _state.asStateFlow()

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.NextStep -> nextStep()
            is OnboardingEvent.PreviousStep -> previousStep()
            is OnboardingEvent.SkipToEnd -> skipToEnd()
            is OnboardingEvent.Complete -> completeOnboarding()
            is OnboardingEvent.SetName -> setName(event.name)
            is OnboardingEvent.SetAge -> setAge(event.age)
            is OnboardingEvent.SelectFocusArea -> selectFocusArea(event.focusArea)
            is OnboardingEvent.SelectReminderTime -> selectReminderTime(event.reminderTime)
        }
    }

    private fun nextStep() {
        _state.update { currentState ->
            if (currentState.currentStep < currentState.totalSteps - 1) {
                currentState.copy(currentStep = currentState.currentStep + 1)
            } else {
                currentState
            }
        }
    }

    private fun previousStep() {
        _state.update { currentState ->
            if (currentState.currentStep > 0) {
                currentState.copy(currentStep = currentState.currentStep - 1)
            } else {
                currentState
            }
        }
    }

    private fun skipToEnd() {
        _state.update { currentState ->
            currentState.copy(currentStep = currentState.totalSteps - 1)
        }
    }

    private fun setName(name: String) {
        _state.update { it.copy(userName = name) }
    }

    private fun setAge(age: Int) {
        _state.update { it.copy(userAge = age) }
    }

    private fun selectFocusArea(focusArea: FocusArea) {
        _state.update { it.copy(selectedFocusArea = focusArea) }
    }

    private fun selectReminderTime(reminderTime: ReminderTime) {
        _state.update { it.copy(selectedReminderTime = reminderTime) }
    }

    private fun completeOnboarding() {
        scope.launch {
            _state.update { it.copy(isLoading = true) }

            val data =
                OnboardingData(
                    userName = _state.value.userName,
                    userAge = _state.value.userAge,
                    focusArea = _state.value.selectedFocusArea,
                    reminderTime = _state.value.selectedReminderTime,
                    isCompleted = true,
                )

            repository.saveOnboardingData(data)
            repository.markOnboardingComplete()

            val reminderEnabled = _state.value.selectedReminderTime != ReminderTime.NONE
            repository.setDailyReminderEnabled(reminderEnabled)

            _state.update { it.copy(isLoading = false, isCompleted = true) }
        }
    }
}
