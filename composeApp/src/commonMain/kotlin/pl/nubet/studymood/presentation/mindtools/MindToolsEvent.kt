package pl.nubet.studymood.presentation.mindtools

import pl.nubet.studymood.domain.model.MindToolCategory

sealed class MindToolsEvent {
    data class CategoryClicked(val category: MindToolCategory) : MindToolsEvent()

    data class SuggestionClicked(val categoryId: String) : MindToolsEvent()

    data class ExerciseSelected(val exerciseId: String) : MindToolsEvent()

    object RandomExercise : MindToolsEvent()

    data class StartExercise(val exerciseId: String) : MindToolsEvent()

    object CloseBottomSheet : MindToolsEvent()
}
