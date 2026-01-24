package pl.nubet.studymood.presentation.breathing

import pl.nubet.studymood.domain.model.BreathingExerciseType

data class BreathingState(
    val exerciseType: BreathingExerciseType,
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val isFinished: Boolean = false,
    val currentCycle: Int = 0,
    val currentPhase: BreathingPhase = BreathingPhase.Ready,
    val phaseTimeLeftSeconds: Int = 0,
    val sessionProgress: Float = 0f,
    val visualizerScale: Float = 0.85f,
)

enum class BreathingPhase {
    Ready,
    Inhale,
    Hold,
    Exhale,
}
