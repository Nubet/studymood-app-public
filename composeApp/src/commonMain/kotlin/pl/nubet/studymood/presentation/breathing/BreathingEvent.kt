package pl.nubet.studymood.presentation.breathing

sealed class BreathingEvent {
    object StartClicked : BreathingEvent()

    object PauseClicked : BreathingEvent()

    object ResumeClicked : BreathingEvent()

    object ResetClicked : BreathingEvent()

    object BackClicked : BreathingEvent()
}
