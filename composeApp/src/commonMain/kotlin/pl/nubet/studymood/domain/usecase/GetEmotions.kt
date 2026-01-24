package pl.nubet.studymood.domain.usecase

import pl.nubet.studymood.data.repository.LexiconRepository
import pl.nubet.studymood.domain.model.Emotion

class GetEmotions(private val lexiconRepository: LexiconRepository) {
    suspend operator fun invoke(): List<Emotion> = lexiconRepository.getAll()
}
