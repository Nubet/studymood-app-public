package pl.nubet.studymood.data.repository

import pl.nubet.studymood.domain.model.Emotion

interface LexiconRepository {
    suspend fun count(): Long

    suspend fun deleteAll()

    suspend fun insertAll(items: List<Emotion>)

    suspend fun getAll(): List<Emotion>

    suspend fun findNearest(pleasant: Int, energy: Int, limit: Int = 3): List<Emotion>
}
