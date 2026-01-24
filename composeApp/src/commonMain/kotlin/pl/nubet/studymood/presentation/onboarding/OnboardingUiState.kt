package pl.nubet.studymood.presentation.onboarding

import pl.nubet.studymood.domain.model.FocusArea
import pl.nubet.studymood.domain.model.ReminderTime

data class OnboardingUiState(
    val currentStep: Int = 0,
    val totalSteps: Int = 8,
    val userName: String = "",
    val userAge: Int = 18,
    val selectedFocusArea: FocusArea = FocusArea.BOTH,
    val selectedReminderTime: ReminderTime = ReminderTime.NONE,
    val isLoading: Boolean = false,
    val isCompleted: Boolean = false,
) {
    val progress: Float
        get() = (currentStep + 1).toFloat() / totalSteps.toFloat()

    val isFirstStep: Boolean
        get() = currentStep == 0

    val isLastStep: Boolean
        get() = currentStep == totalSteps - 1

    val buttonText: String
        get() = if (isLastStep) "Start using StudyMood" else "Continue"

    val microFooterText: String
        get() =
            when (currentStep) {
                in 0..3 -> "Swipe or tap Continue to move through the steps."
                4 -> "You can skip your name, but it keeps the app more personal."
                5 ->
                    "Swipe up or down to set your age. The big number in the frame is the one we use."
                6 -> "This helps us decide which tab to highlight first for you."
                else -> "You can always change reminders in settings later."
            }
}
