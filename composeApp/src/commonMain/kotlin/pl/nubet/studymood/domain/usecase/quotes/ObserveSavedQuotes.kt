package pl.nubet.studymood.domain.usecase.quotes

import kotlinx.coroutines.flow.Flow
import pl.nubet.studymood.data.repository.QuotesRepository

class ObserveSavedQuotes(private val repository: QuotesRepository) {
    operator fun invoke(): Flow<Set<String>> {
        return repository.observeSavedQuotes()
    }
}
