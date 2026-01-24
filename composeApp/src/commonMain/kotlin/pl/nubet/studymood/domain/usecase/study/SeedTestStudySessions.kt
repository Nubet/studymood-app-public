package pl.nubet.studymood.domain.usecase.study

import kotlin.random.Random
import pl.nubet.studymood.core.logging.Log
import pl.nubet.studymood.data.repository.StudyRepository
import pl.nubet.studymood.data.repository.SubjectRepository
import pl.nubet.studymood.domain.model.StudySession
import pl.nubet.studymood.domain.model.Subject

@OptIn(kotlin.time.ExperimentalTime::class)
class SeedTestStudySessions(
    private val studyRepository: StudyRepository,
    private val subjectRepository: SubjectRepository,
) {
    suspend operator fun invoke() {
        createOrGetTestSubjects()

        val testSubjects = subjectRepository.list().filter { it.id.startsWith("test-") }

        if (testSubjects.isEmpty()) {
            Log.i("No test subjects found after creation", tag = "SeedTestStudy")
            return
        }

        Log.i("Using ${testSubjects.size} test subjects for seeding", tag = "SeedTestStudy")

        createTestSessions(testSubjects)
    }

    private suspend fun createOrGetTestSubjects() {
        val existingTestSubjects = subjectRepository.list().filter { it.id.startsWith("test-") }

        if (existingTestSubjects.isNotEmpty()) {
            Log.i(
                "Test subjects already exist (${existingTestSubjects.size})",
                tag = "SeedTestStudy",
            )
            return
        }

        Log.i("Creating test subjects...", tag = "SeedTestStudy")

        val testSubjects =
            listOf(
                Subject(
                    id = "test-math",
                    name = "Math",
                    emoji = "📐",
                    color = null,
                    weeklyGoalMinutes = 300,
                ),
                Subject(
                    id = "test-biology",
                    name = "Biology",
                    emoji = "🧬",
                    color = null,
                    weeklyGoalMinutes = 240,
                ),
                Subject(
                    id = "test-programming",
                    name = "Programming",
                    emoji = "💻",
                    color = null,
                    weeklyGoalMinutes = 480,
                ),
                Subject(
                    id = "test-chemistry",
                    name = "Chemistry",
                    emoji = "⚗️",
                    color = null,
                    weeklyGoalMinutes = 200,
                ),
                Subject(
                    id = "test-physics",
                    name = "Physics",
                    emoji = "🔭",
                    color = null,
                    weeklyGoalMinutes = 250,
                ),
            )

        testSubjects.forEach { subject ->
            try {
                subjectRepository.insert(subject)
                Log.i("Created: ${subject.name}", tag = "SeedTestStudy")
            } catch (e: Exception) {
                Log.e("Failed to create ${subject.name}: ${e.message}", e, tag = "SeedTestStudy")
            }
        }
    }

    private suspend fun createTestSessions(subjects: List<Subject>) {
        val now = kotlin.time.Clock.System.now().toEpochMilliseconds()

        Log.i("Creating test sessions with ${subjects.size} subjects", tag = "SeedTestStudy")

        val sessionDurations = listOf(2, 5, 8, 12, 18, 25, 42, 67, 90, 120)

        sessionDurations.forEachIndexed { index, duration ->
            val subject = subjects[index % subjects.size]

            Log.d(
                "Session ${index + 1}: ${subject.emoji} ${subject.name} - ${duration}min",
                tag = "SeedTestStudy",
            )

            val daysAgo = index % 7
            val hoursAgo = (daysAgo * 24) + (index % 12)
            val sessionStartTime = now - (hoursAgo * 60 * 60 * 1000)
            val sessionEndTime = sessionStartTime + (duration * 60 * 1000)

            val session =
                StudySession(
                    id = "test-session-${Random.nextInt(10000, 99999)}",
                    subjectId = subject.id,
                    startTime = sessionStartTime,
                    endTime = sessionEndTime,
                    durationMinutes = duration,
                    note = "Test session - ${duration} min",
                    pleasant = null,
                    energy = null,
                )

            try {
                studyRepository.insert(session)
            } catch (e: Exception) {
                Log.e("Failed to insert session: ${e.message}", e, tag = "SeedTestStudy")
            }
        }

        Log.i("Finished seeding ${sessionDurations.size} test sessions", tag = "SeedTestStudy")
    }
}
