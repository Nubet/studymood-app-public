package pl.nubet.studymood.presentation.study

import pl.nubet.studymood.domain.model.Subject
import pl.nubet.studymood.domain.model.SubjectWithSessionCount
import pl.nubet.studymood.domain.usecase.study.StudySessionWithSubject

enum class TimerStatus {
    Idle,
    Running,
    Paused,
}

data class StudyUiState(
    val subjects: List<SubjectWithSessionCount> = emptyList(),
    val selectedSubjectId: String? = null,
    val timerStatus: TimerStatus = TimerStatus.Idle,
    val startTime: Long? = null,
    val elapsedSeconds: Long = 0,
    val recentSessions: List<StudySessionWithSubject> = emptyList(),
    val showAddDialog: Boolean = false,
    val addSubjectName: String = "",
    val addSubjectEmoji: String = "📘",
    val showEditSheet: Boolean = false,
    val subjectBeingEdited: Subject? = null,
    val editSubjectName: String = "",
    val editSubjectEmoji: String = "",
    val showMiniMood: Boolean = false,
    val miniPleasant: Int = 60,
    val miniEnergy: Int = 60,
    val isSaving: Boolean = false,
    val error: String? = null,
    val timeOfDayIcon: String = "☀️",
    val microcopy: String = "One focus at a time.",
    val showSessionScreen: Boolean = false,
    val sessionSubject: pl.nubet.studymood.domain.model.Subject? = null,
)
