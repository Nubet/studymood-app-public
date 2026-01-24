package pl.nubet.studymood.domain.usecase.quotes

import pl.nubet.studymood.data.repository.QuotesRepository
import pl.nubet.studymood.domain.model.QuoteCategory

class GetQuoteCategories(private val repository: QuotesRepository) {
    operator fun invoke(): List<QuoteCategory> {
        return repository.getCategories()
    }
}
