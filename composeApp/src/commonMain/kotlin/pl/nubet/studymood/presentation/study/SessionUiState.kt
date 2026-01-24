package pl.nubet.studymood.presentation.study

import pl.nubet.studymood.domain.model.Subject

enum class SessionStatus {
    Running,
    Paused,
    Finished,
}

data class SessionUiState(
    val subject: Subject? = null,
    val sessionStatus: SessionStatus = SessionStatus.Running,
    val startTime: Long = 0L,
    val elapsedSeconds: Long = 0L,
    val showSummarySheet: Boolean = false,
    val sessionDurationFormatted: String = "",
    val sessionStartFormatted: String = "",
    val sessionEndFormatted: String = "",
    val navigateToCheckIn: Boolean = false,
    val navigateToHome: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
)
