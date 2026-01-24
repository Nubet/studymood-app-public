package pl.nubet.studymood.presentation.mindtools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.nubet.studymood.core.logging.Log
import pl.nubet.studymood.core.util.RandomProvider
import pl.nubet.studymood.domain.model.MindToolCategory
import pl.nubet.studymood.domain.usecase.GetExercisesByCategory
import pl.nubet.studymood.domain.usecase.GetMindToolCategories
import pl.nubet.studymood.domain.usecase.GetSuggestedExercise

class MindToolsViewModel(
    private val getCategoriesUseCase: GetMindToolCategories,
    private val getExercisesUseCase: GetExercisesByCategory,
    private val getSuggestedUseCase: GetSuggestedExercise,
    private val randomProvider: RandomProvider,
) : ViewModel() {

    private val _state = MutableStateFlow(MindToolsState())
    val state: StateFlow<MindToolsState> = _state.asStateFlow()

    private val microcopyOptions =
        listOf(
            "Choose a category. Pick one exercise inside. Keep it simple.",
            "Start small. Two minutes is enough to change direction.",
            "Pick the gentlest tool that fits today.",
            "If you cannot decide, start with breathing.",
        )

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val categories = getCategoriesUseCase()
            val suggested = getSuggestedUseCase()
            val microcopy = microcopyOptions[randomProvider.nextInt(microcopyOptions.size)]

            _state.update {
                it.copy(
                    categories = categories,
                    suggestedExercise = suggested,
                    randomMicrocopy = microcopy,
                )
            }
        }
    }

    fun onEvent(event: MindToolsEvent) {
        when (event) {
            is MindToolsEvent.CategoryClicked -> openCategory(event.category)
            is MindToolsEvent.ExerciseSelected -> selectExercise(event.exerciseId)
            MindToolsEvent.CloseBottomSheet -> closeBottomSheet()
            MindToolsEvent.RandomExercise -> pickRandomExercise()
            is MindToolsEvent.StartExercise -> startExercise(event.exerciseId)
            is MindToolsEvent.SuggestionClicked -> {
                val category = MindToolCategory.fromId(event.categoryId)
                if (category != null) openCategory(category)
            }
        }
    }

    private fun openCategory(category: MindToolCategory) {
        val exercises = getExercisesUseCase(category.id)
        _state.update {
            it.copy(
                selectedCategory = category,
                exercises = exercises,
                selectedExerciseId = exercises.firstOrNull()?.id,
                isBottomSheetVisible = true,
            )
        }
    }

    private fun selectExercise(exerciseId: String) {
        _state.update { it.copy(selectedExerciseId = exerciseId) }
    }

    private fun closeBottomSheet() {
        _state.update { it.copy(isBottomSheetVisible = false) }
    }

    private fun pickRandomExercise() {
        val exercises = _state.value.exercises
        if (exercises.isNotEmpty()) {
            val random = exercises.random()
            _state.update { it.copy(selectedExerciseId = random.id) }
        }
    }

    private fun startExercise(exerciseId: String) {
        Log.d("Starting exercise: $exerciseId", tag = "MindToolsViewModel")
        closeBottomSheet()
    }
}
