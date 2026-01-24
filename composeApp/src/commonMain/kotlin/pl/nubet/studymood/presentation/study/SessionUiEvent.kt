package pl.nubet.studymood.presentation.study

sealed class SessionUiEvent {
    data object TogglePauseResume : SessionUiEvent()

    data object FinishSession : SessionUiEvent()

    data object DismissSummarySheet : SessionUiEvent()

    data object NavigateToCheckIn : SessionUiEvent()

    data object SkipCheckIn : SessionUiEvent()

    data object ResetNavigation : SessionUiEvent()
}
