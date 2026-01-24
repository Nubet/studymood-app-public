package pl.nubet.studymood.domain.usecase.quotes

import pl.nubet.studymood.data.repository.QuotesRepository

class ToggleSaveQuote(private val repository: QuotesRepository) {
    suspend operator fun invoke(quoteId: String) {
        repository.toggleSaveQuote(quoteId)
    }
}
