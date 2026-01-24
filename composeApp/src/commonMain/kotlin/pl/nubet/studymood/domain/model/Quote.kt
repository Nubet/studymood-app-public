package pl.nubet.studymood.domain.model

data class Quote(
    val id: String,
    val text: String,
    val author: String,
    val categoryId: String,
)
