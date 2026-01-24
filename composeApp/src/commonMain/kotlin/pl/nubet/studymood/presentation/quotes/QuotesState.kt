package pl.nubet.studymood.presentation.quotes

import pl.nubet.studymood.domain.model.Quote
import pl.nubet.studymood.domain.model.QuoteCategory

data class QuotesState(
    val categories: List<QuoteCategory> = QuoteCategory.all(),
    val selectedCategoryIndex: Int = 0,
    val currentQuotes: List<Quote> = emptyList(),
    val currentQuoteIndex: Int = 0,
    val isMenuOpen: Boolean = false,
    val isHelpModalOpen: Boolean = false,
    val savedQuoteIds: Set<String> = emptySet(),
    val isCopyFeedbackActive: Boolean = false,
    val isVerticalSwipe: Boolean = false,
) {
    val currentCategory: QuoteCategory
        get() = categories.getOrNull(selectedCategoryIndex) ?: QuoteCategory.All

    val currentQuote: Quote?
        get() = currentQuotes.getOrNull(currentQuoteIndex)

    val isCurrentQuoteSaved: Boolean
        get() = currentQuote?.id in savedQuoteIds
}
