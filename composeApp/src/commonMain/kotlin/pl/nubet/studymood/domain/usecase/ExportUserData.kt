package pl.nubet.studymood.domain.usecase

import pl.nubet.studymood.data.repository.MoodRepository
import pl.nubet.studymood.data.repository.OnboardingRepository
import pl.nubet.studymood.data.repository.StudyRepository
import pl.nubet.studymood.data.repository.SubjectRepository

@OptIn(kotlin.time.ExperimentalTime::class)
class ExportUserData(
    private val moodRepository: MoodRepository,
    private val studyRepository: StudyRepository,
    private val subjectRepository: SubjectRepository,
    private val onboardingRepository: OnboardingRepository,
) {
    suspend operator fun invoke(): String {
        val moods = moodRepository.recent(limit = 10000)
        val sessions = studyRepository.recent(limit = 10000)
        val subjects = subjectRepository.list()
        val userName = onboardingRepository.getUserName()
        val onboardingData = onboardingRepository.getOnboardingData()
        val reminderEnabled = onboardingRepository.getDailyReminderEnabled()
        val theme = onboardingRepository.getThemePreference()

        val exportTimestamp = kotlin.time.Clock.System.now().toEpochMilliseconds()

        val sb = StringBuilder()
        sb.appendLine("{")
        sb.appendLine("  \"version\": 1,")
        sb.appendLine("  \"exportTimestamp\": $exportTimestamp,")
        sb.appendLine("  \"userData\": {")
        sb.appendLine("    \"name\": \"${userName.escapeJson()}\",")
        sb.appendLine("    \"age\": ${onboardingData.userAge},")
        sb.appendLine("    \"focusArea\": \"${onboardingData.focusArea.name}\",")
        sb.appendLine("    \"reminderTime\": \"${onboardingData.reminderTime.name}\",")
        sb.appendLine("    \"reminderEnabled\": $reminderEnabled,")
        sb.appendLine("    \"theme\": \"${theme.name}\"")
        sb.appendLine("  },")

        sb.appendLine("  \"moodEntries\": [")
        moods.forEachIndexed { index, mood ->
            sb.appendLine("    {")
            sb.appendLine("      \"id\": \"${mood.id.escapeJson()}\",")
            sb.appendLine("      \"timestamp\": ${mood.timestamp},")
            sb.appendLine("      \"pleasant\": ${mood.pleasant},")
            sb.appendLine("      \"energy\": ${mood.energy},")
            sb.appendLine(
                "      \"labels\": [${mood.labels.joinToString { "\"${it.escapeJson()}\"" }}],"
            )
            sb.appendLine(
                "      \"triggers\": [${mood.triggers.joinToString { "\"${it.escapeJson()}\"" }}],"
            )
            sb.appendLine(
                "      \"note\": ${if (mood.note != null) "\"${mood.note.escapeJson()}\"" else "null"},"
            )
            sb.appendLine("      \"createdAt\": ${mood.createdAt},")
            sb.appendLine("      \"updatedAt\": ${mood.updatedAt}")
            sb.appendLine("    }${if (index < moods.size - 1) "," else ""}")
        }
        sb.appendLine("  ],")

        sb.appendLine("  \"studySessions\": [")
        sessions.forEachIndexed { index, session ->
            sb.appendLine("    {")
            sb.appendLine("      \"id\": \"${session.id.escapeJson()}\",")
            sb.appendLine("      \"subjectId\": \"${session.subjectId.escapeJson()}\",")
            sb.appendLine("      \"startTime\": ${session.startTime},")
            sb.appendLine("      \"endTime\": ${session.endTime},")
            sb.appendLine("      \"durationMinutes\": ${session.durationMinutes},")
            sb.appendLine(
                "      \"note\": ${if (session.note != null) "\"${session.note.escapeJson()}\"" else "null"},"
            )
            sb.appendLine("      \"pleasant\": ${session.pleasant},")
            sb.appendLine("      \"energy\": ${session.energy}")
            sb.appendLine("    }${if (index < sessions.size - 1) "," else ""}")
        }
        sb.appendLine("  ],")

        sb.appendLine("  \"subjects\": [")
        subjects.forEachIndexed { index, subject ->
            sb.appendLine("    {")
            sb.appendLine("      \"id\": \"${subject.id.escapeJson()}\",")
            sb.appendLine("      \"name\": \"${subject.name.escapeJson()}\",")
            sb.appendLine(
                "      \"emoji\": ${if (subject.emoji != null) "\"${subject.emoji.escapeJson()}\"" else "null"},"
            )
            sb.appendLine(
                "      \"color\": ${if (subject.color != null) "\"${subject.color.escapeJson()}\"" else "null"}"
            )
            sb.appendLine("    }${if (index < subjects.size - 1) "," else ""}")
        }
        sb.appendLine("  ]")
        sb.appendLine("}")

        return sb.toString()
    }

    private fun String.escapeJson(): String {
        return this.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }
}
