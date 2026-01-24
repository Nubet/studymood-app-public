package pl.nubet.studymood.data.debug

import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import pl.nubet.studymood.core.logging.Log
import pl.nubet.studymood.data.repository.MoodRepository
import pl.nubet.studymood.data.repository.StudyRepository
import pl.nubet.studymood.data.repository.SubjectRepository
import pl.nubet.studymood.domain.model.MoodEntry
import pl.nubet.studymood.domain.model.StudySession
import pl.nubet.studymood.domain.model.Subject

@OptIn(ExperimentalUuidApi::class)
class DebugDataSeeder(
    private val studyRepository: StudyRepository,
    private val moodRepository: MoodRepository,
    private val subjectRepository: SubjectRepository,
) {

    private val subjectsWithEmoji =
        listOf(
            "Linear Algebra" to "📐",
            "Operating Systems" to "🖥️",
            "Computer Science" to "💻",
            "Computer Architecture" to "🧠",
            "Numerical Methods" to "🔢",
            "Digital Systems" to "🔌",
        )

    private data class HardcodedSession(
        val dayOffset: Int,
        val subject: String,
        val durationMin: Int,
        val hourOfDay: Int,
    )

    private val hardcodedStudySessions =
        listOf(
            HardcodedSession(0, "Linear Algebra", 60, 14),
            HardcodedSession(1, "Computer Science", 50, 10),
            HardcodedSession(1, "Linear Algebra", 45, 16),
            HardcodedSession(2, "Operating Systems", 50, 15),
            HardcodedSession(3, "Linear Algebra", 70, 13),
            HardcodedSession(3, "Numerical Methods", 40, 18),
            HardcodedSession(4, "Computer Science", 90, 9),
            HardcodedSession(5, "Linear Algebra", 55, 11),
            HardcodedSession(6, "Digital Systems", 60, 14),
            HardcodedSession(7, "Linear Algebra", 50, 10),
            HardcodedSession(8, "Computer Architecture", 50, 15),
            HardcodedSession(8, "Computer Science", 60, 19),
            HardcodedSession(9, "Operating Systems", 60, 13),
            HardcodedSession(10, "Linear Algebra", 120, 10),
            HardcodedSession(11, "Numerical Methods", 50, 16),
            HardcodedSession(12, "Computer Science", 40, 14),
            HardcodedSession(13, "Digital Systems", 70, 11),
            HardcodedSession(14, "Linear Algebra", 40, 15),
            HardcodedSession(15, "Operating Systems", 40, 12),
            HardcodedSession(16, "Computer Science", 45, 10),
            HardcodedSession(16, "Linear Algebra", 55, 17),
            HardcodedSession(17, "Computer Architecture", 60, 14),
            HardcodedSession(18, "Numerical Methods", 60, 13),
            HardcodedSession(19, "Linear Algebra", 70, 9),
            HardcodedSession(20, "Computer Science", 50, 16),
            HardcodedSession(21, "Digital Systems", 50, 11),
            HardcodedSession(22, "Linear Algebra", 40, 14),
            HardcodedSession(22, "Operating Systems", 45, 19),
            HardcodedSession(23, "Computer Science", 45, 10),
            HardcodedSession(24, "Computer Architecture", 45, 15),
            HardcodedSession(25, "Numerical Methods", 45, 13),
            HardcodedSession(26, "Linear Algebra", 55, 9),
            HardcodedSession(27, "Digital Systems", 55, 17),
            HardcodedSession(28, "Linear Algebra", 50, 10),
            HardcodedSession(29, "Operating Systems", 50, 14),
            HardcodedSession(29, "Computer Science", 90, 18),
            HardcodedSession(30, "Computer Architecture", 50, 12),
            HardcodedSession(31, "Numerical Methods", 50, 15),
            HardcodedSession(32, "Linear Algebra", 70, 10),
            HardcodedSession(33, "Digital Systems", 60, 13),
            HardcodedSession(35, "Linear Algebra", 40, 9),
            HardcodedSession(36, "Operating Systems", 60, 14),
            HardcodedSession(37, "Computer Science", 60, 11),
            HardcodedSession(37, "Linear Algebra", 55, 18),
            HardcodedSession(38, "Computer Architecture", 60, 15),
            HardcodedSession(39, "Numerical Methods", 60, 10),
            HardcodedSession(41, "Linear Algebra", 50, 14),
            HardcodedSession(43, "Computer Science", 40, 16),
            HardcodedSession(49, "Computer Architecture", 40, 10),
            HardcodedSession(50, "Numerical Methods", 40, 15),
            HardcodedSession(50, "Linear Algebra", 70, 20),
            HardcodedSession(51, "Digital Systems", 50, 9),
            HardcodedSession(52, "Linear Algebra", 40, 14),
            HardcodedSession(53, "Operating Systems", 45, 11),
            HardcodedSession(54, "Computer Science", 45, 17),
            HardcodedSession(55, "Computer Architecture", 30, 10),
            HardcodedSession(56, "Numerical Methods", 45, 14),
            HardcodedSession(56, "Linear Algebra", 120, 19),
            HardcodedSession(57, "Linear Algebra", 55, 9),
            HardcodedSession(58, "Digital Systems", 150, 13),
            HardcodedSession(59, "Computer Science", 65, 16),
        )

    private data class HardcodedMoodCheck(
        val dayOffset: Int,
        val hour: Int,
        val emotion: String,
        val pleasant: Int,
        val energy: Int,
        val secondLabel: String? = null,
        val trigger: String? = null,
        val note: String? = null,
    )

    private val hardcodedMoodChecks =
        listOf(
            HardcodedMoodCheck(0, 8, "motivated", 75, 70, null, null, null),
            HardcodedMoodCheck(0, 13, "focused", 80, 75, null, null, null),
            HardcodedMoodCheck(0, 18, "content", 70, 30, null, null, null),
            HardcodedMoodCheck(1, 7, "calm", 75, 25, null, "good sleep", null),
            HardcodedMoodCheck(1, 14, "focused", 80, 75, null, null, null),
            HardcodedMoodCheck(1, 19, "satisfied", 75, 30, null, null, null),
            HardcodedMoodCheck(2, 10, "confident", 80, 65, null, null, null),
            HardcodedMoodCheck(2, 15, "productive", 80, 70, null, null, null),
            HardcodedMoodCheck(2, 20, "relaxed", 75, 30, null, null, null),
            HardcodedMoodCheck(3, 8, "energetic", 85, 80, "motivated", null, "Feeling good today"),
            HardcodedMoodCheck(3, 16, "happy", 85, 70, null, null, null),
            HardcodedMoodCheck(3, 21, "satisfied", 70, 35, null, null, null),
            HardcodedMoodCheck(4, 11, "productive", 80, 75, null, "good study session", null),
            HardcodedMoodCheck(4, 17, "focused", 75, 70, null, null, null),
            HardcodedMoodCheck(4, 20, "peaceful", 75, 25, null, null, null),
            HardcodedMoodCheck(5, 9, "happy", 85, 70, null, null, null),
            HardcodedMoodCheck(5, 14, "content", 80, 35, null, null, null),
            HardcodedMoodCheck(6, 12, "calm", 75, 30, null, null, null),
            HardcodedMoodCheck(6, 18, "comfortable", 75, 30, null, "rest day", null),
            HardcodedMoodCheck(7, 7, "calm", 70, 25, null, null, null),
            HardcodedMoodCheck(7, 13, "motivated", 75, 70, null, null, null),
            HardcodedMoodCheck(7, 19, "focused", 75, 75, null, null, null),
            HardcodedMoodCheck(8, 10, "motivated", 80, 70, null, null, null),
            HardcodedMoodCheck(8, 16, "productive", 80, 75, null, null, null),
            HardcodedMoodCheck(8, 22, "content", 70, 30, null, null, null),
            HardcodedMoodCheck(9, 12, "productive", 85, 80, "focused", null, "Great progress"),
            HardcodedMoodCheck(9, 20, "satisfied", 75, 35, null, null, null),
            HardcodedMoodCheck(10, 8, "energetic", 80, 75, null, "exercise", null),
            HardcodedMoodCheck(10, 15, "happy", 80, 70, null, null, null),
            HardcodedMoodCheck(10, 19, "satisfied", 75, 35, null, null, null),
            HardcodedMoodCheck(11, 14, "confident", 75, 65, null, null, null),
            HardcodedMoodCheck(12, 11, "calm", 75, 30, null, null, null),
            HardcodedMoodCheck(13, 17, "relaxed", 70, 25, null, "social time", null),
            HardcodedMoodCheck(14, 8, "focused", 70, 70, null, null, null),
            HardcodedMoodCheck(14, 20, "tired", 30, 25, null, null, null),
            HardcodedMoodCheck(15, 10, "anxious", 30, 75, null, "assignment deadline", null),
            HardcodedMoodCheck(15, 18, "calm", 65, 30, null, null, null),
            HardcodedMoodCheck(16, 9, "motivated", 70, 65, null, null, null),
            HardcodedMoodCheck(16, 21, "content", 70, 30, null, null, null),
            HardcodedMoodCheck(17, 13, "focused", 75, 75, null, null, null),
            HardcodedMoodCheck(18, 11, "stressed", 25, 70, null, "exam preparation", null),
            HardcodedMoodCheck(18, 19, "tired", 30, 30, null, null, null),
            HardcodedMoodCheck(19, 7, "nervous", 30, 65, "worried", null, "Worried about exams"),
            HardcodedMoodCheck(20, 15, "calm", 70, 30, null, null, null),
            HardcodedMoodCheck(21, 8, "anxious", 25, 70, null, "exam preparation", null),
            HardcodedMoodCheck(21, 20, "tired", 25, 25, null, "lack of sleep", null),
            HardcodedMoodCheck(22, 10, "stressed", 30, 75, "pressured", "deadline pressure", null),
            HardcodedMoodCheck(
                23,
                12,
                "overwhelmed",
                20,
                75,
                null,
                "too much work",
                "Too much to do",
            ),
            HardcodedMoodCheck(23, 21, "drained", 25, 20, null, null, null),
            HardcodedMoodCheck(24, 9, "focused", 65, 70, null, null, null),
            HardcodedMoodCheck(25, 14, "frustrated", 30, 65, null, null, null),
            HardcodedMoodCheck(26, 11, "tired", 30, 30, null, "lack of sleep", null),
            HardcodedMoodCheck(27, 16, "calm", 70, 30, null, "rest day", null),
            HardcodedMoodCheck(28, 7, "anxious", 30, 70, null, null, null),
            HardcodedMoodCheck(28, 19, "tired", 30, 25, null, null, null),
            HardcodedMoodCheck(29, 10, "stressed", 25, 75, null, "exam preparation", null),
            HardcodedMoodCheck(29, 22, "exhausted", 20, 15, null, "lack of sleep", "Need sleep"),
            HardcodedMoodCheck(30, 13, "pressured", 25, 70, null, "deadline pressure", null),
            HardcodedMoodCheck(31, 11, "overwhelmed", 20, 70, "anxious", "too much work", null),
            HardcodedMoodCheck(32, 8, "tired", 25, 20, null, null, null),
            HardcodedMoodCheck(32, 18, "frustrated", 30, 65, null, null, null),
            HardcodedMoodCheck(33, 14, "nervous", 30, 65, null, "exam preparation", null),
            HardcodedMoodCheck(35, 9, "anxious", 25, 75, "stressed", "multiple exams", null),
            HardcodedMoodCheck(
                36,
                12,
                "overwhelmed",
                20,
                70,
                null,
                "can't keep up",
                "Feeling overwhelmed",
            ),
            HardcodedMoodCheck(37, 10, "tired", 20, 25, "drained", null, null),
            HardcodedMoodCheck(37, 20, "satisfied", 70, 35, null, null, "Finally done"),
            HardcodedMoodCheck(38, 14, "relaxed", 75, 30, null, "rest day", null),
            HardcodedMoodCheck(39, 16, "content", 75, 30, null, null, null),
            HardcodedMoodCheck(41, 11, "peaceful", 80, 25, null, null, null),
            HardcodedMoodCheck(42, 15, "content", 75, 30, null, "social time", null),
            HardcodedMoodCheck(43, 13, "calm", 75, 25, null, null, null),
            HardcodedMoodCheck(45, 17, "relaxed", 80, 30, null, null, null),
            HardcodedMoodCheck(49, 7, "tired", 20, 15, "drained", null, "Exhausted"),
            HardcodedMoodCheck(49, 10, "anxious", 25, 70, null, "exam preparation", null),
            HardcodedMoodCheck(
                49,
                21,
                "exhausted",
                15,
                10,
                "burned out",
                "lack of sleep",
                "Can't do this anymore",
            ),
            HardcodedMoodCheck(50, 8, "drained", 15, 10, null, "lack of sleep", null),
            HardcodedMoodCheck(50, 14, "stressed", 20, 75, "overwhelmed", "too much work", null),
            HardcodedMoodCheck(50, 20, "frustrated", 20, 70, null, "deadline pressure", null),
            HardcodedMoodCheck(51, 7, "exhausted", 20, 15, null, null, "Just want to sleep"),
            HardcodedMoodCheck(51, 12, "overwhelmed", 15, 70, "anxious", "multiple exams", null),
            HardcodedMoodCheck(51, 18, "frustrated", 25, 65, null, "deadline pressure", null),
            HardcodedMoodCheck(
                52,
                11,
                "overwhelmed",
                15,
                70,
                "anxious",
                "multiple exams",
                "Too much pressure",
            ),
            HardcodedMoodCheck(52, 19, "stressed", 20, 75, null, "can't keep up", null),
            HardcodedMoodCheck(
                52,
                22,
                "burned out",
                15,
                10,
                "tired",
                "exhausted",
                "Breaking point",
            ),
            HardcodedMoodCheck(53, 9, "sad", 20, 20, "discouraged", null, null),
            HardcodedMoodCheck(53, 16, "anxious", 20, 70, "nervous", "exam preparation", null),
            HardcodedMoodCheck(53, 19, "stressed", 20, 75, null, "can't keep up", null),
            HardcodedMoodCheck(54, 8, "fatigued", 20, 15, null, null, "So tired of everything"),
            HardcodedMoodCheck(54, 12, "tired", 15, 15, "drained", null, null),
            HardcodedMoodCheck(54, 21, "anxious", 20, 70, "nervous", "exam preparation", null),
            HardcodedMoodCheck(
                55,
                7,
                "exhausted",
                10,
                10,
                "burned out",
                "lack of sleep",
                "Completely drained",
            ),
            HardcodedMoodCheck(55, 12, "frustrated", 20, 70, "stressed", "multiple exams", null),
            HardcodedMoodCheck(
                55,
                18,
                "overwhelmed",
                15,
                75,
                "pressured",
                "deadline pressure",
                null,
            ),
            HardcodedMoodCheck(55, 23, "tired", 15, 15, "drained", null, null),
            HardcodedMoodCheck(
                56,
                8,
                "burned out",
                10,
                10,
                "exhausted",
                "too much work",
                "Need to stop but can't",
            ),
            HardcodedMoodCheck(
                56,
                13,
                "reflective",
                30,
                20,
                null,
                null,
                "Thinking about giving up",
            ),
            HardcodedMoodCheck(56, 20, "frustrated", 20, 70, "stressed", "multiple exams", null),
            HardcodedMoodCheck(
                57,
                6,
                "drained",
                15,
                10,
                "fatigued",
                null,
                "Exhausted beyond words",
            ),
            HardcodedMoodCheck(
                57,
                11,
                "surprised",
                40,
                60,
                null,
                "good test result",
                "Didn't expect that",
            ),
            HardcodedMoodCheck(57, 14, "anxious", 20, 75, "overwhelmed", "exam preparation", null),
            HardcodedMoodCheck(57, 22, "sad", 15, 20, "discouraged", null, "Breaking point"),
            HardcodedMoodCheck(
                58,
                9,
                "stressed",
                15,
                70,
                "pressured",
                "can't keep up",
                "Feeling completely overwhelmed",
            ),
            HardcodedMoodCheck(58, 14, "reflective", 25, 15, null, null, "Questioning everything"),
            HardcodedMoodCheck(58, 19, "tired", 15, 15, "burned out", "exhausted", null),
            HardcodedMoodCheck(59, 10, "overwhelmed", 15, 75, "anxious", "deadline pressure", null),
            HardcodedMoodCheck(59, 16, "drained", 10, 10, "fatigued", null, null),
            HardcodedMoodCheck(
                59,
                21,
                "exhausted",
                10,
                10,
                "drained",
                "lack of sleep",
                "Just want this to end",
            ),
        )

    suspend fun seedIfNeeded() {
        val existingSessions =
            try {
                studyRepository.recent(limit = 1)
            } catch (_: Exception) {
                emptyList()
            }

        if (existingSessions.isNotEmpty()) {
            Log.i("[Seeder] Data already exists, skipping seed", tag = "Seeder")
            return
        }

        Log.i("[Seeder] Starting debug data seeding...", tag = "Seeder")

        val subjectIds = seedSubjects()
        seedStudySessions(subjectIds)
        seedMoodEntries()

        Log.i("[Seeder] Mock data seeded successfully!", tag = "Seeder")
    }

    private suspend fun seedSubjects(): List<String> {
        Log.d("Seeding subjects...", tag = "Seeder")
        val subjectIds =
            subjectsWithEmoji.map { (name, emoji) ->
                val id = Uuid.random().toString()
                subjectRepository.insert(
                    Subject(
                        id = id,
                        name = name,
                        emoji = emoji,
                        color = null,
                        weeklyGoalMinutes = Random.nextInt(300, 600),
                    )
                )
                id
            }
        Log.i("Created ${subjectIds.size} subjects", tag = "Seeder")
        return subjectIds
    }

    private suspend fun seedStudySessions(subjectIds: List<String>) {
        Log.d("Seeding study sessions...", tag = "Seeder")

        val startDate = 1763020800000L
        val oneDayMillis = 24L * 60 * 60 * 1000

        val subjectNameToId = subjectsWithEmoji.map { it.first }.zip(subjectIds).toMap()

        hardcodedStudySessions.forEach { session ->
            val sessionDate = startDate + (session.dayOffset * oneDayMillis)
            val sessionTime = sessionDate + (session.hourOfDay * 60 * 60 * 1000L)
            val subjectId = subjectNameToId[session.subject] ?: subjectIds.first()

            val studySession =
                StudySession(
                    id = Uuid.random().toString(),
                    subjectId = subjectId,
                    startTime = sessionTime,
                    endTime = sessionTime + (session.durationMin * 60 * 1000L),
                    durationMinutes = session.durationMin,
                    note = null,
                    pleasant = 3,
                    energy = 3,
                )

            studyRepository.insert(studySession)
        }

        Log.i("Generated ${hardcodedStudySessions.size} study sessions", tag = "Seeder")
    }

    private suspend fun seedMoodEntries() {
        Log.d("Seeding mood check-ins...", tag = "Seeder")

        val startDate = 1763020800000L

        hardcodedMoodChecks.forEach { check ->
            val checkTime =
                startDate +
                    (check.dayOffset * 24L * 60 * 60 * 1000) +
                    (check.hour * 60 * 60 * 1000L)

            val labels = listOfNotNull(check.emotion, check.secondLabel)
            val triggers = listOfNotNull(check.trigger)

            val moodEntry =
                MoodEntry(
                    id = Uuid.random().toString(),
                    timestamp = checkTime,
                    pleasant = check.pleasant,
                    energy = check.energy,
                    labels = labels,
                    triggers = triggers,
                    note = check.note,
                    createdAt = checkTime,
                    updatedAt = checkTime,
                )

            moodRepository.insert(moodEntry)
        }

        Log.i("Generated ${hardcodedMoodChecks.size} mood check-ins", tag = "Seeder")
    }
}
