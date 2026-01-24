package pl.nubet.studymood.presentation.study

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.nubet.studymood.core.util.ClockProvider
import pl.nubet.studymood.core.util.MainScopeProvider
import pl.nubet.studymood.domain.constants.StudyConstants
import pl.nubet.studymood.domain.model.StudySession
import pl.nubet.studymood.domain.usecase.study.AddSubject
import pl.nubet.studymood.domain.usecase.study.DeleteSubject
import pl.nubet.studymood.domain.usecase.study.GetRecentSessionsWithSubjects
import pl.nubet.studymood.domain.usecase.study.GetSubjectsWithSessionCount
import pl.nubet.studymood.domain.usecase.study.SaveStudySession
import pl.nubet.studymood.domain.usecase.study.UpdateSubject

class StudyViewModel(
    private val addSubject: AddSubject,
    private val getSubjectsWithCount: GetSubjectsWithSessionCount,
    private val saveStudy: SaveStudySession,
    private val getRecentSessionsWithSubjects: GetRecentSessionsWithSubjects,
    private val updateSubject: UpdateSubject,
    private val deleteSubject: DeleteSubject,
    private val scopeProvider: MainScopeProvider,
    private val clockProvider: ClockProvider,
) {

    private val _state = MutableStateFlow(StudyUiState())
    val state: StateFlow<StudyUiState> = _state

    private val timer =
        SessionTimer(
            scope = scopeProvider.scope,
            clockProvider = clockProvider,
            onTick = { seconds -> _state.update { it.copy(elapsedSeconds = seconds) } },
        )

    init {
        initializeTimeAndMicrocopy()
        refreshData()
    }

    private fun initializeTimeAndMicrocopy() {
        val nowMillis = clockProvider.nowMillis()

        val hour = ((nowMillis / (1000 * 60 * 60)) % 24).toInt()
        val icon = StudyConstants.getTimeOfDayIcon(hour)
        val quote = StudyConstants.getRandomMicrocopy()
        _state.update { it.copy(timeOfDayIcon = icon, microcopy = quote) }
    }

    fun onEvent(event: StudyUiEvent) {
        when (event) {
            is StudyUiEvent.SelectSubject -> {
                _state.update { it.copy(selectedSubjectId = event.subjectId) }
            }
            is StudyUiEvent.SubjectCardLongPressed -> {
                val subjectWithCount =
                    _state.value.subjects.find { it.subject.id == event.subjectId }
                if (subjectWithCount != null) {
                    _state.update {
                        it.copy(
                            showEditSheet = true,
                            subjectBeingEdited = subjectWithCount.subject,
                            editSubjectName = subjectWithCount.subject.name,
                            editSubjectEmoji = subjectWithCount.subject.emoji ?: "",
                        )
                    }
                }
            }

            StudyUiEvent.Start -> startSession()
            StudyUiEvent.Pause -> pause()
            StudyUiEvent.Resume -> resume()
            StudyUiEvent.Stop -> stop()
            StudyUiEvent.StartStudySessionClicked -> startSession()
            StudyUiEvent.CloseSessionScreen -> handleCloseSessionScreen()

            StudyUiEvent.RefreshData -> refreshData()

            is StudyUiEvent.UpdateMiniMood -> {
                _state.update {
                    it.copy(
                        miniPleasant = event.pleasant ?: it.miniPleasant,
                        miniEnergy = event.energy ?: it.miniEnergy,
                    )
                }
            }
            StudyUiEvent.SaveSession -> scopeProvider.scope.launch { saveSession() }

            StudyUiEvent.DismissMiniMood -> _state.update { it.copy(showMiniMood = false) }

            StudyUiEvent.AddSubjectClicked -> {
                _state.update {
                    it.copy(showAddDialog = true, addSubjectName = "", addSubjectEmoji = "📘")
                }
            }
            is StudyUiEvent.SubjectNameChanged -> {
                _state.update { it.copy(addSubjectName = event.name) }
            }
            is StudyUiEvent.SubjectEmojiChanged -> {
                _state.update { it.copy(addSubjectEmoji = event.emoji) }
            }
            StudyUiEvent.ConfirmAddSubject ->
                scopeProvider.scope.launch {
                    val name = _state.value.addSubjectName.trim()
                    if (name.isNotEmpty()) {
                        addSubject(name, _state.value.addSubjectEmoji, null, null)
                            .fold(
                                onSuccess = {
                                    _state.update { s ->
                                        s.copy(
                                            showAddDialog = false,
                                            selectedSubjectId = it.id,
                                            addSubjectName = "",
                                            addSubjectEmoji = "📘",
                                        )
                                    }
                                    refreshData()
                                },
                                onFailure = { err ->
                                    _state.update { s -> s.copy(error = err.message) }
                                },
                            )
                    }
                }
            StudyUiEvent.DismissAddSubjectDialog -> {
                _state.update {
                    it.copy(showAddDialog = false, addSubjectName = "", addSubjectEmoji = "📘")
                }
            }

            StudyUiEvent.DismissEditSheet -> {
                _state.update { it.copy(showEditSheet = false, subjectBeingEdited = null) }
            }
            is StudyUiEvent.RenameSubject ->
                scopeProvider.scope.launch {
                    val subjectId = _state.value.subjectBeingEdited?.id ?: return@launch
                    if (event.newName.trim().isNotEmpty()) {
                        updateSubject(subjectId, name = event.newName.trim())
                            .fold(
                                onSuccess = {
                                    _state.update {
                                        it.copy(showEditSheet = false, subjectBeingEdited = null)
                                    }
                                    refreshData()
                                },
                                onFailure = { err ->
                                    _state.update { it.copy(error = err.message) }
                                },
                            )
                    }
                }
            is StudyUiEvent.ChangeSubjectEmoji ->
                scopeProvider.scope.launch {
                    val subjectId = _state.value.subjectBeingEdited?.id ?: return@launch
                    if (event.newEmoji.isNotEmpty()) {
                        updateSubject(subjectId, emoji = event.newEmoji)
                            .fold(
                                onSuccess = {
                                    _state.update {
                                        it.copy(showEditSheet = false, subjectBeingEdited = null)
                                    }
                                    refreshData()
                                },
                                onFailure = { err ->
                                    _state.update { it.copy(error = err.message) }
                                },
                            )
                    }
                }
            StudyUiEvent.DeleteSubject ->
                scopeProvider.scope.launch {
                    val subjectId = _state.value.subjectBeingEdited?.id ?: return@launch
                    deleteSubject(subjectId)
                        .fold(
                            onSuccess = {
                                _state.update {
                                    it.copy(
                                        showEditSheet = false,
                                        subjectBeingEdited = null,
                                        selectedSubjectId = null,
                                    )
                                }
                                refreshData()
                            },
                            onFailure = { err -> _state.update { it.copy(error = err.message) } },
                        )
                }

            is StudyUiEvent.ShowAddDialog -> {
                _state.update { it.copy(showAddDialog = event.show) }
            }
            is StudyUiEvent.AddSubject ->
                scopeProvider.scope.launch {
                    addSubject(event.name, event.emoji, event.color, null)
                        .fold(
                            onSuccess = {
                                _state.update { s ->
                                    s.copy(showAddDialog = false, selectedSubjectId = it.id)
                                }
                                refreshData()
                            },
                            onFailure = { err ->
                                _state.update { s -> s.copy(error = err.message) }
                            },
                        )
                }
        }
    }

    private fun startSession() {
        val subjectId = state.value.selectedSubjectId ?: return
        val subjectWithCount = state.value.subjects.find { it.subject.id == subjectId } ?: return
        timer.reset()
        _state.update {
            it.copy(
                showSessionScreen = true,
                sessionSubject = subjectWithCount.subject,
                timerStatus = TimerStatus.Running,
                startTime = timer.nowMillis(),
                elapsedSeconds = 0,
            )
        }
        timer.start()
    }

    private fun pause() {
        if (state.value.timerStatus != TimerStatus.Running) return
        timer.pause()
        _state.update { it.copy(timerStatus = TimerStatus.Paused) }
    }

    private fun resume() {
        if (state.value.timerStatus != TimerStatus.Paused) return
        _state.update { it.copy(timerStatus = TimerStatus.Running) }
        timer.resume()
    }

    private fun stop() {
        if (state.value.timerStatus == TimerStatus.Idle) return
        timer.stop()
        _state.update { it.copy(timerStatus = TimerStatus.Idle, showMiniMood = true) }
    }

    private suspend fun saveSession() {
        val s = state.value
        val start = s.startTime ?: return
        val end = timer.nowMillis()

        val durationMinutes = (s.elapsedSeconds / 60).toInt()
        val session =
            StudySession(
                id = "ss_${end}",
                subjectId = s.selectedSubjectId ?: return,
                startTime = start,
                endTime = end,
                durationMinutes = durationMinutes,
                note = null,
                pleasant = s.miniPleasant,
                energy = s.miniEnergy,
            )
        _state.update { it.copy(isSaving = true) }
        saveStudy(session)
        _state.update {
            it.copy(isSaving = false, showMiniMood = false, elapsedSeconds = 0, startTime = null)
        }
    }

    private fun handleCloseSessionScreen() {
        timer.stop()
        _state.update { it.copy(showSessionScreen = false, sessionSubject = null) }
        refreshData()
    }

    private fun refreshData() {
        refreshSubjects()
        refreshRecentSessions()
    }

    private fun refreshSubjects() {
        scopeProvider.scope.launch {
            val list = getSubjectsWithCount()
            _state.update { it.copy(subjects = list) }
        }
    }

    private fun refreshRecentSessions() {
        scopeProvider.scope.launch {
            val withSubjects = getRecentSessionsWithSubjects(10)
            _state.update { it.copy(recentSessions = withSubjects) }
        }
    }
}
