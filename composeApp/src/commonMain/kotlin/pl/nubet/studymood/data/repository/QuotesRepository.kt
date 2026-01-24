package pl.nubet.studymood.data.repository

import kotlinx.coroutines.flow.Flow
import pl.nubet.studymood.domain.model.Quote
import pl.nubet.studymood.domain.model.QuoteCategory

interface QuotesRepository {
    fun getCategories(): List<QuoteCategory>

    fun getQuotesByCategory(categoryId: String): List<Quote>

    fun getAllQuotes(): List<Quote>

    fun observeSavedQuotes(): Flow<Set<String>>

    suspend fun toggleSaveQuote(quoteId: String)

    fun isSaved(quoteId: String): Boolean
}
