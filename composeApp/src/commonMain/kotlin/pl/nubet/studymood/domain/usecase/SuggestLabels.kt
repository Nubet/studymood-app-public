package pl.nubet.studymood.domain.usecase

import pl.nubet.studymood.data.repository.LexiconRepository
import pl.nubet.studymood.domain.model.Emotion

class SuggestLabels(private val lexiconRepository: LexiconRepository) {
    suspend operator fun invoke(pleasant: Int, energy: Int, limit: Int = 3): List<Emotion> {
        return lexiconRepository.findNearest(pleasant, energy, limit)
    }
}
