package pl.nubet.studymood.presentation.quotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.nubet.studymood.domain.usecase.quotes.GetQuoteCategories
import pl.nubet.studymood.domain.usecase.quotes.GetQuotesByCategory
import pl.nubet.studymood.domain.usecase.quotes.ObserveSavedQuotes
import pl.nubet.studymood.domain.usecase.quotes.ToggleSaveQuote

class QuotesViewModel(
    private val getCategoriesUseCase: GetQuoteCategories,
    private val getQuotesUseCase: GetQuotesByCategory,
    private val toggleSaveUseCase: ToggleSaveQuote,
    private val observeSavedUseCase: ObserveSavedQuotes,
) : ViewModel() {

    private val _state = MutableStateFlow(QuotesState())
    val state: StateFlow<QuotesState> = _state.asStateFlow()

    init {
        loadData()
        observeSavedQuotes()
    }

    private fun loadData() {
        val categories = getCategoriesUseCase()
        val quotes = getQuotesUseCase("all")

        _state.update { it.copy(categories = categories, currentQuotes = quotes) }
    }

    private fun observeSavedQuotes() {
        viewModelScope.launch {
            observeSavedUseCase().collect { savedIds ->
                _state.update { it.copy(savedQuoteIds = savedIds) }
            }
        }
    }

    fun onEvent(event: QuotesEvent) {
        when (event) {
            QuotesEvent.OnBackClicked -> {}
            QuotesEvent.OnHelpClicked -> openHelpModal()
            QuotesEvent.OnHelpDismissed -> closeHelpModal()
            QuotesEvent.OnMenuToggled -> toggleMenu()
            QuotesEvent.OnMenuDismissed -> closeMenu()
            is QuotesEvent.OnCategorySelected -> selectCategory(event.index)
            is QuotesEvent.OnSwipeHorizontal -> handleHorizontalSwipe(event.direction)
            is QuotesEvent.OnSwipeVertical -> handleVerticalSwipe(event.direction)
            QuotesEvent.OnSaveToggled -> toggleSave()
            QuotesEvent.OnCopyClicked -> triggerCopyFeedback()
        }
    }

    private fun openHelpModal() {
        _state.update { it.copy(isHelpModalOpen = true) }
    }

    private fun closeHelpModal() {
        _state.update { it.copy(isHelpModalOpen = false) }
    }

    private fun toggleMenu() {
        _state.update { it.copy(isMenuOpen = !it.isMenuOpen) }
    }

    private fun closeMenu() {
        _state.update { it.copy(isMenuOpen = false) }
    }

    private fun selectCategory(index: Int, isVerticalSwipe: Boolean = false) {
        val category = _state.value.categories.getOrNull(index) ?: return
        val quotes = getQuotesUseCase(category.id)

        _state.update {
            it.copy(
                selectedCategoryIndex = index,
                currentQuotes = quotes,
                currentQuoteIndex = 0,
                isMenuOpen = false,
                isVerticalSwipe = isVerticalSwipe,
            )
        }
    }

    private fun handleHorizontalSwipe(direction: SwipeDirection) {
        val quotes = _state.value.currentQuotes
        if (quotes.isEmpty()) return

        val newIndex =
            when (direction) {
                SwipeDirection.NEXT -> (_state.value.currentQuoteIndex + 1) % quotes.size
                SwipeDirection.PREV ->
                    (_state.value.currentQuoteIndex - 1 + quotes.size) % quotes.size
                else -> return
            }

        _state.update { it.copy(currentQuoteIndex = newIndex, isVerticalSwipe = false) }
    }

    private fun handleVerticalSwipe(direction: SwipeDirection) {
        val categories = _state.value.categories
        val currentIndex = _state.value.selectedCategoryIndex

        val newIndex =
            when (direction) {
                SwipeDirection.DOWN -> (currentIndex + 1) % categories.size
                SwipeDirection.UP -> (currentIndex - 1 + categories.size) % categories.size
                else -> return
            }

        selectCategory(newIndex, isVerticalSwipe = true)
    }

    private fun toggleSave() {
        val quoteId = _state.value.currentQuote?.id ?: return
        viewModelScope.launch { toggleSaveUseCase(quoteId) }
    }

    private fun triggerCopyFeedback() {
        _state.update { it.copy(isCopyFeedbackActive = true) }

        viewModelScope.launch {
            delay(1500)
            _state.update { it.copy(isCopyFeedbackActive = false) }
        }
    }
}
