package pl.nubet.studymood.presentation.mood

import kotlin.math.max
import kotlin.math.min
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.nubet.studymood.core.Config
import pl.nubet.studymood.core.logging.Log
import pl.nubet.studymood.core.util.ClockProvider
import pl.nubet.studymood.core.util.MainScopeProvider
import pl.nubet.studymood.core.util.RandomProvider
import pl.nubet.studymood.domain.model.getQuadrantFromCoordinates
import pl.nubet.studymood.domain.model.toInt
import pl.nubet.studymood.domain.usecase.EnsureLexiconSeeded
import pl.nubet.studymood.domain.usecase.FilterEmotionsUseCase
import pl.nubet.studymood.domain.usecase.GetEmotions
import pl.nubet.studymood.domain.usecase.GetRecentMoods
import pl.nubet.studymood.domain.usecase.SaveMoodEntry
import pl.nubet.studymood.domain.usecase.SuggestLabels
import pl.nubet.studymood.domain.usecase.mood.BuildMoodEntry
import pl.nubet.studymood.domain.usecase.mood.CalculateMoodStats

class CheckInViewModel(
    private val ensureLexiconSeeded: EnsureLexiconSeeded,
    private val getEmotions: GetEmotions,
    private val filterEmotionsUseCase: FilterEmotionsUseCase,
    private val suggestLabels: SuggestLabels,
    private val saveMood: SaveMoodEntry,
    private val getRecentMoods: GetRecentMoods,
    private val buildMoodEntry: BuildMoodEntry,
    private val calculateMoodStats: CalculateMoodStats,
    private val scopeProvider: MainScopeProvider,
    private val clockProvider: ClockProvider,
    private val randomProvider: RandomProvider,
) {

    private val _state = MutableStateFlow(CheckInUiState())
    val state: StateFlow<CheckInUiState> = _state

    init {
        scopeProvider.scope.launch {
            ensureLexiconSeeded()
            val all = getEmotions()
            _state.update { it.copy(availableEmotions = all) }
            filterEmotions()
        }
        scopeProvider.scope.launch { refreshSuggestions() }
        scopeProvider.scope.launch { loadMoodStats() }

        recomputeCanSave()
    }

    fun onEvent(event: CheckInUiEvent) {
        when (event) {
            CheckInUiEvent.StartCheckIn -> {
                _state.update { it.copy(isPreCheck = false) }
            }
            is CheckInUiEvent.OnPointChange -> {
                val p = clamp(event.pleasant)
                val e = clamp(event.energy)
                _state.update { it.copy(pleasant = p, energy = e, errorMsg = null) }
                recomputeCanSave()
                scopeProvider.scope.launch { refreshSuggestions() }
            }
            is CheckInUiEvent.OnSelectEmotion -> {
                _state.update { it.copy(selectedEmotion = event.emotion, errorMsg = null) }
                recomputeCanSave()
                scopeProvider.scope.launch { refreshSuggestions() }
            }
            is CheckInUiEvent.OnNoteChange -> {
                val text = event.note.take(Config.NOTE_MAX)
                _state.update { it.copy(note = text) }
                recomputeCanSave()
            }
            CheckInUiEvent.OnSave -> {
                scopeProvider.scope.launch { save() }
            }

            CheckInUiEvent.OpenPicker -> {
                _state.update { it.copy(showEmotionPicker = true) }
                scopeProvider.scope.launch {
                    if (state.value.availableEmotions.isEmpty()) {
                        val all = getEmotions()
                        _state.update { it.copy(availableEmotions = all) }
                    }
                }
            }
            CheckInUiEvent.ClosePicker -> _state.update { it.copy(showEmotionPicker = false) }
            is CheckInUiEvent.SearchQueryChanged -> {
                _state.update { it.copy(searchQuery = event.query) }
                filterEmotions()
            }
            is CheckInUiEvent.QuadrantFilterChanged -> {
                _state.update { it.copy(selectedQuadrantFilter = event.quadrant) }
                filterEmotions()
            }
            CheckInUiEvent.ClearError -> _state.update { it.copy(errorMsg = null) }
            is CheckInUiEvent.ShowError -> _state.update { it.copy(errorMsg = event.message) }
            CheckInUiEvent.ContinueToContext -> {
                _state.update { it.copy(showContextScreen = true) }
            }
            is CheckInUiEvent.OnContextComplete -> {
                _state.update {
                    it.copy(
                        activity = event.activity,
                        companion = event.companion,
                        location = event.location,
                    )
                }
                scopeProvider.scope.launch { saveWithContext() }
            }
        }
    }

    private fun filterEmotions() {
        val s = state.value
        val filtered =
            filterEmotionsUseCase(
                emotions = s.availableEmotions,
                query = s.searchQuery,
                quadrant = s.selectedQuadrantFilter,
            )
        _state.update { it.copy(filteredEmotions = filtered) }
    }

    private suspend fun refreshSuggestions() {
        val s = state.value
        val base = suggestLabels(s.pleasant, s.energy, 6)
        val q = quadrantOf(s.pleasant, s.energy)
        val filtered = base.filter { it.quadrant == q }.ifEmpty { base }
        _state.update { it.copy(suggestions = filtered.take(3)) }
    }

    private suspend fun save() {
        val s = state.value
        if (s.isSaving) return
        val selected = s.selectedEmotion
        if (selected == null) {
            _state.update { it.copy(errorMsg = "Please select an emotion before saving.") }
            recomputeCanSave()
            return
        }
        val qPoint = quadrantOf(s.pleasant, s.energy)
        if (selected.quadrant != qPoint) {
            _state.update { it.copy(errorMsg = "Selected emotion doesn't match current quadrant.") }
            recomputeCanSave()
            return
        }
        if (!s.canSave) return
        _state.update { it.copy(isSaving = true) }

        val entry =
            buildMoodEntry(
                selectedEmotion = selected,
                pleasant = s.pleasant,
                energy = s.energy,
                note = s.note.ifBlank { null },
                triggers = emptyList(),
            )
        saveMood(entry)
        _state.update {
            it.copy(isSaving = false, note = "", showEmotionPicker = false, errorMsg = null)
        }
    }

    private suspend fun saveWithContext() {
        val s = state.value
        val selected = s.selectedEmotion ?: return
        _state.update { it.copy(isSaving = true) }

        val entry =
            buildMoodEntry(
                selectedEmotion = selected,
                pleasant = s.pleasant,
                energy = s.energy,
                note = null,
                triggers =
                    buildList {
                        if (s.activity.isNotBlank()) add(s.activity)
                        s.companion?.let { add(it) }
                        s.location?.let { add(it) }
                    },
            )

        try {
            Log.d("About to save mood entry: ${entry.id}", tag = "CheckInViewModel")
            saveMood(entry)
            Log.i("Mood entry saved successfully: ${entry.id}", tag = "CheckInViewModel")

            loadMoodStats()
        } catch (e: Exception) {
            Log.e("Error saving mood entry: ${e.message}", e, tag = "CheckInViewModel")
            _state.update { it.copy(isSaving = false, errorMsg = "Failed to save: ${e.message}") }
            return
        }

        _state.update {
            it.copy(
                isSaving = false,
                isPreCheck = true,
                showContextScreen = false,
                selectedEmotion = null,
                activity = "",
                companion = null,
                location = null,
                pleasant = 50,
                energy = 50,
                errorMsg = null,
            )
        }
    }

    private fun clamp(v: Int) = min(100, max(0, v))

    private fun quadrantOf(pleasant: Int, energy: Int): Int =
        getQuadrantFromCoordinates(pleasant, energy).toInt()

    private fun recomputeCanSave() {
        val s = state.value
        val hasEmotion = s.selectedEmotion != null
        val noteOk = s.note.length <= Config.NOTE_MAX
        val quadrantOk = s.selectedEmotion?.quadrant == quadrantOf(s.pleasant, s.energy)
        _state.update { it.copy(canSave = hasEmotion && quadrantOk && noteOk) }
    }

    private suspend fun loadMoodStats() {
        try {
            val stats = calculateMoodStats(limit = 1000)
            _state.update {
                it.copy(
                    totalMoodsLogged = stats.totalMoodsLogged,
                    currentStreakDays = stats.currentStreakDays,
                )
            }
        } catch (e: Exception) {
            Log.e("Error loading mood stats: ${e.message}", e, tag = "CheckInViewModel")
        }
    }
}
