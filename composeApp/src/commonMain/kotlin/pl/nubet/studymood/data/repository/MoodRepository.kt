package pl.nubet.studymood.data.repository

import pl.nubet.studymood.domain.model.MoodEntry

interface MoodRepository {
    suspend fun insert(entry: MoodEntry)

    suspend fun recent(limit: Int = 50): List<MoodEntry>

    suspend fun deleteAll()
}
