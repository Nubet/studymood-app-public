package pl.nubet.studymood.domain.model

data class OnboardingData(
    val userName: String = "",
    val userAge: Int = 18,
    val focusArea: FocusArea = FocusArea.BOTH,
    val reminderTime: ReminderTime = ReminderTime.NONE,
    val isCompleted: Boolean = false,
)

enum class FocusArea {
    MOOD,
    STUDY,
    BOTH,
}

enum class ReminderTime(val hour: Int, val displayName: String) {
    EVENING(21, "Evening wind down · 21:00"),
    AFTER_SCHOOL(17, "After school or work · 17:00"),
    NONE(-1, "No reminders for now"),
}
