package pl.nubet.studymood.presentation.mood

import pl.nubet.studymood.domain.model.Emotion

sealed class CheckInUiEvent {
    data class OnPointChange(val pleasant: Int, val energy: Int) : CheckInUiEvent()

    data class OnSelectEmotion(val emotion: Emotion) : CheckInUiEvent()

    data class OnNoteChange(val note: String) : CheckInUiEvent()

    data object OnSave : CheckInUiEvent()

    data object StartCheckIn : CheckInUiEvent()

    data object OpenPicker : CheckInUiEvent()

    data object ClosePicker : CheckInUiEvent()

    data class SearchQueryChanged(val query: String) : CheckInUiEvent()

    data class QuadrantFilterChanged(val quadrant: Int?) : CheckInUiEvent()

    data object ClearError : CheckInUiEvent()

    data class ShowError(val message: String) : CheckInUiEvent()

    data object ContinueToContext : CheckInUiEvent()

    data class OnContextComplete(
        val activity: String,
        val companion: String?,
        val location: String?,
    ) : CheckInUiEvent()
}
