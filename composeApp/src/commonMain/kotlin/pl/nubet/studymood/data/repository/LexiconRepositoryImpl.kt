package pl.nubet.studymood.data.repository

import pl.nubet.studymood.data.db.StudyMoodDatabase
import pl.nubet.studymood.domain.model.Emotion

class LexiconRepositoryImpl(private val database: StudyMoodDatabase) : LexiconRepository {
    private val queries
        get() = database.emotion_lexiconQueries

    override suspend fun count(): Long = queries.countAll().executeAsOne()

    override suspend fun deleteAll() {
        queries.deleteAll()
    }

    override suspend fun insertAll(items: List<Emotion>) {
        queries.transaction {
            items.forEach { e ->
                queries.insertItem(
                    id = e.id,
                    label = e.label,
                    synonym = e.synonym,
                    description = e.description,
                    pleasant_default = e.pleasantDefault.toLong(),
                    energy_default = e.energyDefault.toLong(),
                    quadrant = e.quadrant.toLong(),
                    custom = if (e.custom) 1L else 0L,
                )
            }
        }
    }

    override suspend fun getAll(): List<Emotion> =
        queries.getAll().executeAsList().map { row ->
            Emotion(
                id = row.id,
                label = row.label,
                synonym = row.synonym,
                description = row.description,
                pleasantDefault = row.pleasant_default.toInt(),
                energyDefault = row.energy_default.toInt(),
                quadrant = row.quadrant.toInt(),
                custom = row.custom.toInt() != 0,
            )
        }

    override suspend fun findNearest(pleasant: Int, energy: Int, limit: Int): List<Emotion> =
        queries
            .findNearest(pleasant.toLong(), energy.toLong(), limit.toLong())
            .executeAsList()
            .map { row ->
                Emotion(
                    id = row.id,
                    label = row.label,
                    synonym = row.synonym,
                    description = row.description,
                    pleasantDefault = row.pleasant_default.toInt(),
                    energyDefault = row.energy_default.toInt(),
                    quadrant = row.quadrant.toInt(),
                    custom = row.custom.toInt() != 0,
                )
            }
}
