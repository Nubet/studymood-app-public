package pl.nubet.studymood.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pl.nubet.studymood.data.db.StudyMoodDatabase
import pl.nubet.studymood.domain.model.FocusArea
import pl.nubet.studymood.domain.model.OnboardingData
import pl.nubet.studymood.domain.model.ReminderTime
import pl.nubet.studymood.domain.model.ThemePreference

class OnboardingRepositoryImpl(private val database: StudyMoodDatabase) : OnboardingRepository {

    private val queries
        get() = database.user_preferencesQueries

    private val _themeFlow = MutableStateFlow(ThemePreference.LIGHT)

    override suspend fun saveOnboardingData(data: OnboardingData) {
        queries.insertPreferences(
            user_name = data.userName,
            user_age = data.userAge.toLong(),
            focus_area = data.focusArea.name,
            reminder_time = data.reminderTime.name,
            reminder_hour = data.reminderTime.hour.toLong(),
            is_onboarding_completed = if (data.isCompleted) 1L else 0L,
            daily_reminder_enabled = 0L,
            theme_preference = "LIGHT",
        )
    }

    override suspend fun getOnboardingData(): OnboardingData {
        val row = queries.getPreferences().executeAsOneOrNull()

        if (row == null) {
            return OnboardingData()
        }

        return OnboardingData(
            userName = row.user_name,
            userAge = row.user_age.toInt(),
            focusArea = FocusArea.valueOf(row.focus_area),
            reminderTime = ReminderTime.valueOf(row.reminder_time),
            isCompleted = row.is_onboarding_completed == 1L,
        )
    }

    override suspend fun markOnboardingComplete() {
        val current = getOnboardingData()
        val reminderEnabled = getDailyReminderEnabled()
        val theme = getThemePreference()

        queries.updatePreferences(
            user_name = current.userName,
            user_age = current.userAge.toLong(),
            focus_area = current.focusArea.name,
            reminder_time = current.reminderTime.name,
            reminder_hour = current.reminderTime.hour.toLong(),
            is_onboarding_completed = 1L,
            daily_reminder_enabled = if (reminderEnabled) 1L else 0L,
            theme_preference = theme.name,
        )
    }

    override suspend fun isOnboardingCompleted(): Boolean {
        val result = queries.isOnboardingCompleted().executeAsOneOrNull()
        return result == 1L
    }

    override suspend fun getUserName(): String {
        val result = queries.getUserName().executeAsOneOrNull()
        return result ?: ""
    }

    override suspend fun setUserName(name: String) {
        queries.setUserName(name)
    }

    override suspend fun resetOnboarding() {
        queries.resetOnboarding()
    }

    override suspend fun setDailyReminderEnabled(enabled: Boolean) {
        queries.setDailyReminderEnabled(if (enabled) 1L else 0L)
    }

    override suspend fun getDailyReminderEnabled(): Boolean {
        val result = queries.getDailyReminderEnabled().executeAsOneOrNull()
        return result == 1L
    }

    override suspend fun setReminderTime(reminderTime: ReminderTime) {
        queries.setReminderTime(
            reminder_time = reminderTime.name,
            reminder_hour = reminderTime.hour.toLong(),
        )
    }

    override suspend fun getReminderTime(): ReminderTime {
        val result = queries.getReminderTime().executeAsOneOrNull()
        return if (result != null) {
            try {
                ReminderTime.valueOf(result)
            } catch (_: IllegalArgumentException) {
                ReminderTime.NONE
            }
        } else {
            ReminderTime.NONE
        }
    }

    override suspend fun setThemePreference(theme: ThemePreference) {
        queries.setThemePreference(theme.name)
        _themeFlow.value = theme
    }

    override suspend fun getThemePreference(): ThemePreference {
        val result = queries.getThemePreference().executeAsOneOrNull()
        return ThemePreference.fromString(result)
    }

    override fun observeThemePreference(): Flow<ThemePreference> {
        return _themeFlow.asStateFlow()
    }
}
