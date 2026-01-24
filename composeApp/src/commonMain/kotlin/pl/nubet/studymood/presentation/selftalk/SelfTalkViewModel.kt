package pl.nubet.studymood.presentation.selftalk

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pl.nubet.studymood.domain.model.SelfTalkSteps
import pl.nubet.studymood.presentation.common.StepNavigator

class SelfTalkViewModel : ViewModel() {

    private val navigator = StepNavigator(SelfTalkSteps.TOTAL_STEPS)

    private val _state = MutableStateFlow(SelfTalkState())
    val state = _state.asStateFlow()

    fun onEvent(event: SelfTalkEvent) {
        when (event) {
            is SelfTalkEvent.NextClicked -> moveToNextStep()
            is SelfTalkEvent.BackClicked -> moveToPreviousStep()
            is SelfTalkEvent.FinishClicked -> completeExercise()
            is SelfTalkEvent.NavigateBack -> {}
            is SelfTalkEvent.CheckInClicked -> {}
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

    fun getCurrentStep() = SelfTalkSteps.steps.getOrNull(_state.value.currentStepIndex)
}
