package pl.nubet.studymood.presentation.mood

import pl.nubet.studymood.domain.model.Emotion

data class CheckInUiState(
    val pleasant: Int = 50,
    val energy: Int = 50,
    val selectedEmotion: Emotion? = null,
    val suggestions: List<Emotion> = emptyList(),
    val note: String = "",
    val canSave: Boolean = false,
    val isSaving: Boolean = false,
    val isPreCheck: Boolean = true,
    val totalMoodsLogged: Int = 0,
    val currentStreakDays: Int = 0,
    val showEmotionPicker: Boolean = false,
    val searchQuery: String = "",
    val selectedQuadrantFilter: Int? = null,
    val errorMsg: String? = null,
    val availableEmotions: List<Emotion> = emptyList(),
    val filteredEmotions: List<Emotion> = emptyList(),
    val showContextScreen: Boolean = false,
    val activity: String = "",
    val companion: String? = null,
    val location: String? = null,
)
