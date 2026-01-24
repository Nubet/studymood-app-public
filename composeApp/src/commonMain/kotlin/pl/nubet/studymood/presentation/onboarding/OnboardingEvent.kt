package pl.nubet.studymood.presentation.onboarding

import pl.nubet.studymood.domain.model.FocusArea
import pl.nubet.studymood.domain.model.ReminderTime

sealed class OnboardingEvent {
    data object NextStep : OnboardingEvent()

    data object PreviousStep : OnboardingEvent()

    data object SkipToEnd : OnboardingEvent()

    data object Complete : OnboardingEvent()

    data class SetName(val name: String) : OnboardingEvent()

    data class SetAge(val age: Int) : OnboardingEvent()

    data class SelectFocusArea(val focusArea: FocusArea) : OnboardingEvent()

    data class SelectReminderTime(val reminderTime: ReminderTime) : OnboardingEvent()
}
