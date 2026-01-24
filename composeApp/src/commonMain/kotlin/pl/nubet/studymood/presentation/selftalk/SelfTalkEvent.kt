package pl.nubet.studymood.presentation.selftalk

sealed interface SelfTalkEvent {
    data object NextClicked : SelfTalkEvent

    data object BackClicked : SelfTalkEvent

    data object FinishClicked : SelfTalkEvent

    data object NavigateBack : SelfTalkEvent

    data object CheckInClicked : SelfTalkEvent
}
