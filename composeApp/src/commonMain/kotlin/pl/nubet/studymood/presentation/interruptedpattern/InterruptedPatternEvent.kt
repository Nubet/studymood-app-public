package pl.nubet.studymood.presentation.interruptedpattern

import pl.nubet.studymood.domain.model.NormalizedPoint

sealed interface InterruptedPatternEvent {
    data class PointerDown(val normalizedPoint: NormalizedPoint) : InterruptedPatternEvent

    data class PointerMove(val normalizedPoint: NormalizedPoint) : InterruptedPatternEvent

    data object PointerUp : InterruptedPatternEvent

    data object PhaseTimerTick : InterruptedPatternEvent

    data object DoneClicked : InterruptedPatternEvent

    data object BackClicked : InterruptedPatternEvent
}
