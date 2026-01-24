package pl.nubet.studymood.domain.model

sealed class QuoteCategory(val id: String, val name: String) {
    data object All : QuoteCategory("all", "All Quotes")

    data object Stress : QuoteCategory("stress", "Stress")

    data object Positivity : QuoteCategory("positivity", "Positivity")

    data object Balance : QuoteCategory("balance", "Balance")

    companion object {
        fun all(): List<QuoteCategory> = listOf(All, Stress, Positivity, Balance)

        fun fromId(id: String): QuoteCategory? =
            when (id) {
                "all" -> All
                "stress" -> Stress
                "positivity" -> Positivity
                "balance" -> Balance
                else -> null
            }
    }
}
