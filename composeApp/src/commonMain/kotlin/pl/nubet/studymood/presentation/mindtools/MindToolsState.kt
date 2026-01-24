package pl.nubet.studymood.presentation.mindtools

import pl.nubet.studymood.domain.model.MindToolCategory
import pl.nubet.studymood.domain.model.MindToolExercise

data class MindToolsState(
    val categories: List<MindToolCategory> = emptyList(),
    val selectedCategory: MindToolCategory? = null,
    val exercises: List<MindToolExercise> = emptyList(),
    val selectedExerciseId: String? = null,
    val isBottomSheetVisible: Boolean = false,
    val suggestedExercise: Pair<MindToolCategory, MindToolExercise>? = null,
    val randomMicrocopy: String = "",
)
