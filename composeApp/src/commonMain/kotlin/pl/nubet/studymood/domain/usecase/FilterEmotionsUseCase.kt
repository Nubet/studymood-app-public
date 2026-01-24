package pl.nubet.studymood.domain.usecase

import pl.nubet.studymood.domain.model.Emotion

class FilterEmotionsUseCase {
    operator fun invoke(
        emotions: List<Emotion>,
        query: String,
        quadrant: Int? = null,
    ): List<Emotion> {
        return emotions
            .filter { matchesSearch(it, query) }
            .filter { matchesQuadrant(it, quadrant) }
            .sortedBy { it.label }
    }

    private fun matchesSearch(emotion: Emotion, query: String): Boolean {
        if (query.isEmpty()) return true

        val normalizedQuery = query.lowercase()
        val labelMatches = emotion.label.lowercase().contains(normalizedQuery)
        val synonymMatches = emotion.synonym?.lowercase()?.contains(normalizedQuery) == true

        return labelMatches || synonymMatches
    }

    private fun matchesQuadrant(emotion: Emotion, quadrant: Int?): Boolean {
        return quadrant == null || emotion.quadrant == quadrant
    }
}
