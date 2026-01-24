package pl.nubet.studymood.presentation.breathing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.nubet.studymood.core.util.ClockProvider
import pl.nubet.studymood.domain.model.BreathingExerciseConfig
import pl.nubet.studymood.domain.model.BreathingExerciseType

class BreathingViewModel(
    private val exerciseType: BreathingExerciseType,
    private val clockProvider: ClockProvider,
) : ViewModel() {

    private val _state = MutableStateFlow(BreathingState(exerciseType))
    val state = _state.asStateFlow()

    private var startTimeMs: Long = 0
    private var elapsedBeforePauseMs: Long = 0
    private var timerJob: Job? = null

    fun onEvent(event: BreathingEvent) {
        when (event) {
            is BreathingEvent.StartClicked -> startSession()
            is BreathingEvent.PauseClicked -> pauseSession()
            is BreathingEvent.ResumeClicked -> resumeSession()
            is BreathingEvent.ResetClicked -> resetSession()
            is BreathingEvent.BackClicked -> {}
        }
    }

    private fun startSession() {
        startTimeMs = clockProvider.nowMillis()
        _state.update { it.copy(isRunning = true, isPaused = false) }
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob =
            viewModelScope.launch {
                while (_state.value.isRunning && !_state.value.isFinished) {
                    delay(50)
                    updateBreathingState()
                }
            }
    }

    private fun updateBreathingState() {
        val config = exerciseType.config
        val totalElapsedMs = (clockProvider.nowMillis() - startTimeMs) + elapsedBeforePauseMs

        if (totalElapsedMs >= config.totalDurationMs) {
            finishSession()
            return
        }

        val timeInCycleMs = totalElapsedMs % config.cycleDurationMs
        val currentCycle = (totalElapsedMs / config.cycleDurationMs).toInt()

        val (phase, phaseTimeLeftMs, scale) = calculatePhase(timeInCycleMs, config)

        _state.update {
            it.copy(
                currentCycle = currentCycle,
                currentPhase = phase,
                phaseTimeLeftSeconds = (phaseTimeLeftMs / 1000).toInt() + 1,
                sessionProgress = totalElapsedMs.toFloat() / config.totalDurationMs,
                visualizerScale = scale,
            )
        }
    }

    private fun calculatePhase(
        timeInCycleMs: Long,
        config: BreathingExerciseConfig,
    ): Triple<BreathingPhase, Long, Float> {
        return when {
            timeInCycleMs < config.inhaleMs -> {
                val progress = timeInCycleMs.toFloat() / config.inhaleMs
                Triple(
                    BreathingPhase.Inhale,
                    config.inhaleMs - timeInCycleMs,
                    0.85f + (0.25f * progress),
                )
            }
            timeInCycleMs < config.inhaleMs + config.hold1Ms -> {
                Triple(
                    BreathingPhase.Hold,
                    (config.inhaleMs + config.hold1Ms) - timeInCycleMs,
                    1.1f,
                )
            }
            timeInCycleMs < config.inhaleMs + config.hold1Ms + config.exhaleMs -> {
                val progress =
                    (timeInCycleMs - (config.inhaleMs + config.hold1Ms)).toFloat() / config.exhaleMs
                Triple(
                    BreathingPhase.Exhale,
                    (config.inhaleMs + config.hold1Ms + config.exhaleMs) - timeInCycleMs,
                    1.1f - (0.25f * progress),
                )
            }
            else -> {
                Triple(BreathingPhase.Hold, config.cycleDurationMs - timeInCycleMs, 0.85f)
            }
        }
    }

    private fun finishSession() {
        _state.update {
            it.copy(
                isRunning = false,
                isFinished = true,
                sessionProgress = 1f,
                currentPhase = BreathingPhase.Ready,
                visualizerScale = 1f,
                currentCycle = exerciseType.config.maxCycles,
            )
        }
        timerJob?.cancel()
    }

    private fun pauseSession() {
        elapsedBeforePauseMs += clockProvider.nowMillis() - startTimeMs
        _state.update { it.copy(isRunning = false, isPaused = true) }
        timerJob?.cancel()
    }

    private fun resumeSession() {
        startTimeMs = clockProvider.nowMillis()
        _state.update { it.copy(isRunning = true, isPaused = false) }
        startTimer()
    }

    private fun resetSession() {
        timerJob?.cancel()
        startTimeMs = 0
        elapsedBeforePauseMs = 0
        _state.value = BreathingState(exerciseType)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
