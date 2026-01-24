package pl.nubet.studymood.data.repository

import pl.nubet.studymood.core.logging.Log
import pl.nubet.studymood.data.db.StudyMoodDatabase
import pl.nubet.studymood.domain.model.MoodEntry

class MoodRepositoryImpl(private val database: StudyMoodDatabase) : MoodRepository {
    private val queries
        get() = database.mood_entryQueries

    override suspend fun insert(entry: MoodEntry) {
        Log.d("insert called for entry: ${entry.id}", tag = "MoodRepository")
        try {
            queries.insertEntry(
                id = entry.id,
                timestamp = entry.timestamp,
                pleasant = entry.pleasant.toLong(),
                energy = entry.energy.toLong(),
                labels = entry.labels.joinToString(prefix = "[", postfix = "]") { "\"$it\"" },
                triggers = entry.triggers.joinToString(prefix = "[", postfix = "]") { "\"$it\"" },
                note = entry.note,
                created_at = entry.createdAt,
                updated_at = entry.updatedAt,
            )
            Log.i("insert completed successfully for entry: ${entry.id}", tag = "MoodRepository")
        } catch (e: Exception) {
            Log.e("insert failed: ${e.message}", e, tag = "MoodRepository")
            throw e
        }
    }

    override suspend fun recent(limit: Int): List<MoodEntry> =
        queries.recent(limit.toLong()).executeAsList().map { row ->
            MoodEntry(
                id = row.id,
                timestamp = row.timestamp,
                pleasant = row.pleasant.toInt(),
                energy = row.energy.toInt(),
                labels = parseJsonArray(row.labels),
                triggers = parseJsonArray(row.triggers),
                note = row.note,
                createdAt = row.created_at,
                updatedAt = row.updated_at,
            )
        }

    override suspend fun deleteAll() {
        queries.deleteAll()
    }
}

private fun parseJsonArray(json: String?): List<String> {
    if (json.isNullOrBlank()) return emptyList()
    return json.trim().removePrefix("[").removeSuffix("]").split(',').mapNotNull {
        it.trim().trim('"').takeIf { s -> s.isNotEmpty() }
    }
}
