package pl.nubet.studymood.domain.usecase.quotes

import pl.nubet.studymood.data.repository.QuotesRepository
import pl.nubet.studymood.domain.model.Quote

class GetQuotesByCategory(private val repository: QuotesRepository) {
    operator fun invoke(categoryId: String): List<Quote> {
        return repository.getQuotesByCategory(categoryId)
    }
}
