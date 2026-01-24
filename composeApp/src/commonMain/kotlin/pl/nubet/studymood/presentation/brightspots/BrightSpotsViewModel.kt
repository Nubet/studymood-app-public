package pl.nubet.studymood.presentation.brightspots

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pl.nubet.studymood.domain.model.BrightSpotsSteps
import pl.nubet.studymood.presentation.common.StepNavigator

class BrightSpotsViewModel : ViewModel() {

    private val navigator = StepNavigator(BrightSpotsSteps.TOTAL_STEPS)

    private val _state = MutableStateFlow(BrightSpotsState())
    val state = _state.asStateFlow()

    fun onEvent(event: BrightSpotsEvent) {
        when (event) {
            is BrightSpotsEvent.NextClicked -> moveToNextStep()
            is BrightSpotsEvent.BackClicked -> moveToPreviousStep()
            is BrightSpotsEvent.FinishClicked -> completeExercise()
            is BrightSpotsEvent.NavigateBack -> {}
            is BrightSpotsEvent.CheckInClicked -> {}
        }
    }

    private fun moveToNextStep() {
        navigator.next()
        _state.update { it.copy(currentStepIndex = navigator.currentIndex) }
    }

    private fun moveToPreviousStep() {
        navigator.prev()
        _state.update { it.copy(currentStepIndex = navigator.currentIndex) }
    }

    private fun completeExercise() {
        navigator.complete()
        _state.update { it.copy(isCompleted = true) }
    }

    fun getCurrentStep() = BrightSpotsSteps.steps.getOrNull(_state.value.currentStepIndex)
}
