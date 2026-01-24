package pl.nubet.studymood.presentation.study

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.nubet.studymood.core.util.ClockProvider
import pl.nubet.studymood.core.util.MainScopeProvider
import pl.nubet.studymood.domain.model.StudySession
import pl.nubet.studymood.domain.model.Subject
import pl.nubet.studymood.domain.usecase.study.SaveStudySession

class SessionViewModel(
    private val subject: Subject,
    private val saveStudy: SaveStudySession,
    private val scopeProvider: MainScopeProvider,
    private val clockProvider: ClockProvider,
) {

    private val _state = MutableStateFlow(SessionUiState(subject = subject))
    val state: StateFlow<SessionUiState> = _state

    private val timer =
        SessionTimer(
            scope = scopeProvider.scope,
            clockProvider = clockProvider,
            onTick = { seconds -> _state.update { it.copy(elapsedSeconds = seconds) } },
        )
    private var sessionStartTime: Long = 0L
    private var sessionEndTime: Long = 0L

    init {
        startSession()
    }

    private fun startSession() {
        sessionStartTime = timer.nowMillis()
        timer.reset()
        _state.update {
            it.copy(
                sessionStatus = SessionStatus.Running,
                startTime = sessionStartTime,
                elapsedSeconds = 0L,
            )
        }
        timer.start()
    }

    fun onEvent(event: SessionUiEvent) {
        when (event) {
            SessionUiEvent.TogglePauseResume -> togglePauseResume()
            SessionUiEvent.FinishSession -> finishSession()
            SessionUiEvent.DismissSummarySheet -> dismissSummary()
            SessionUiEvent.NavigateToCheckIn -> navigateToCheckIn()
            SessionUiEvent.SkipCheckIn -> skipCheckIn()
            SessionUiEvent.ResetNavigation -> resetNavigation()
        }
    }

    private fun togglePauseResume() {
        val currentStatus = _state.value.sessionStatus
        when (currentStatus) {
            SessionStatus.Running -> {
                timer.pause()
                _state.update { it.copy(sessionStatus = SessionStatus.Paused) }
            }
            SessionStatus.Paused -> {
                _state.update { it.copy(sessionStatus = SessionStatus.Running) }
                timer.resume()
            }

            SessionStatus.Finished -> {}
        }
    }

    private fun finishSession() {
        timer.stop()
        sessionEndTime = timer.nowMillis()

        val elapsedSecs = _state.value.elapsedSeconds
        val durationFormatted = timer.formatDuration(elapsedSecs)
        val startFormatted = timer.formatTime(sessionStartTime)
        val endFormatted = timer.formatTime(sessionEndTime)

        _state.update {
            it.copy(
                sessionStatus = SessionStatus.Finished,
                showSummarySheet = true,
                sessionDurationFormatted = durationFormatted,
                sessionStartFormatted = startFormatted,
                sessionEndFormatted = endFormatted,
            )
        }

        scopeProvider.scope.launch { saveSessionToDatabase() }
    }

    private suspend fun saveSessionToDatabase() {
        _state.update { it.copy(isSaving = true) }

        val elapsedSecs = _state.value.elapsedSeconds
        val durationMinutes = (elapsedSecs / 60).toInt().coerceAtLeast(1)

        val session =
            StudySession(
                id = "session_${sessionEndTime}",
                subjectId = subject.id,
                startTime = sessionStartTime,
                endTime = sessionEndTime,
                durationMinutes = durationMinutes,
                note = null,
                pleasant = null,
                energy = null,
            )

        try {
            saveStudy(session)
            _state.update { it.copy(isSaving = false) }
        } catch (e: Exception) {
            _state.update {
                it.copy(isSaving = false, error = "Failed to save session: ${e.message}")
            }
        }
    }

    private fun dismissSummary() {
        _state.update { it.copy(showSummarySheet = false) }
    }

    private fun navigateToCheckIn() {
        _state.update { it.copy(navigateToCheckIn = true) }
    }

    private fun skipCheckIn() {
        _state.update { it.copy(navigateToHome = true) }
    }

    private fun resetNavigation() {
        _state.update { it.copy(navigateToCheckIn = false, navigateToHome = false) }
    }

    fun getTimerDisplay(): String {
        return timer.formatTimerDisplay(_state.value.elapsedSeconds)
    }

    fun cleanup() {
        timer.stop()
    }
}
