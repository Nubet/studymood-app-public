package pl.nubet.studymood.presentation.quotes

enum class SwipeDirection {
    NEXT,
    PREV,
    UP,
    DOWN,
}

sealed class QuotesEvent {
    data object OnBackClicked : QuotesEvent()

    data object OnHelpClicked : QuotesEvent()

    data object OnHelpDismissed : QuotesEvent()

    data object OnMenuToggled : QuotesEvent()

    data object OnMenuDismissed : QuotesEvent()

    data class OnCategorySelected(val index: Int) : QuotesEvent()

    data class OnSwipeHorizontal(val direction: SwipeDirection) : QuotesEvent()

    data class OnSwipeVertical(val direction: SwipeDirection) : QuotesEvent()

    data object OnSaveToggled : QuotesEvent()

    data object OnCopyClicked : QuotesEvent()
}
