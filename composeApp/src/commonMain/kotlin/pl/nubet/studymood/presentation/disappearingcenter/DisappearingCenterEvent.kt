package pl.nubet.studymood.presentation.disappearingcenter

import pl.nubet.studymood.domain.model.NormalizedPoint

sealed interface DisappearingCenterEvent {
    data class PointerDown(val normalizedPoint: NormalizedPoint) : DisappearingCenterEvent

    data class PointerMove(val normalizedPoint: NormalizedPoint) : DisappearingCenterEvent

    data object PointerUp : DisappearingCenterEvent

    data object PhaseTimerTick : DisappearingCenterEvent

    data object DoneClicked : DisappearingCenterEvent

    data object BackClicked : DisappearingCenterEvent
}
