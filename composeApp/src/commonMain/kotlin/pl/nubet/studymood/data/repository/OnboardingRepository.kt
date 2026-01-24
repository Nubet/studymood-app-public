package pl.nubet.studymood.data.repository

import kotlinx.coroutines.flow.Flow
import pl.nubet.studymood.domain.model.OnboardingData
import pl.nubet.studymood.domain.model.ReminderTime
import pl.nubet.studymood.domain.model.ThemePreference

interface OnboardingRepository {
    suspend fun saveOnboardingData(data: OnboardingData)

    suspend fun getOnboardingData(): OnboardingData

    suspend fun markOnboardingComplete()

    suspend fun isOnboardingCompleted(): Boolean

    suspend fun getUserName(): String

    suspend fun setUserName(name: String)

    suspend fun resetOnboarding()

    suspend fun setDailyReminderEnabled(enabled: Boolean)

    suspend fun getDailyReminderEnabled(): Boolean

    suspend fun setReminderTime(reminderTime: ReminderTime)

    suspend fun getReminderTime(): ReminderTime

    suspend fun setThemePreference(theme: ThemePreference)

    suspend fun getThemePreference(): ThemePreference

    fun observeThemePreference(): Flow<ThemePreference>
}
