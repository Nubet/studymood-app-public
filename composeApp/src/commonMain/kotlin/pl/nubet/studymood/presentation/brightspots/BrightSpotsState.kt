package pl.nubet.studymood.presentation.brightspots

data class BrightSpotsState(
    val currentStepIndex: Int = 0,
    val isCompleted: Boolean = false,
    val totalSteps: Int = 6,
) {
    val canGoBack: Boolean
        get() = currentStepIndex > 0 && !isCompleted

    val canGoForward: Boolean
        get() = currentStepIndex < totalSteps - 1 || !isCompleted

    val progressText: String
        get() = if (isCompleted) "Done" else "${currentStepIndex + 1}/$totalSteps"
}
