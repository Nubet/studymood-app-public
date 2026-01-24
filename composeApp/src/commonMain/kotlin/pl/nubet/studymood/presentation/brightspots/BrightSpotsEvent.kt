package pl.nubet.studymood.presentation.brightspots

sealed interface BrightSpotsEvent {
    data object NextClicked : BrightSpotsEvent

    data object BackClicked : BrightSpotsEvent

    data object FinishClicked : BrightSpotsEvent

    data object NavigateBack : BrightSpotsEvent

    data object CheckInClicked : BrightSpotsEvent
}
