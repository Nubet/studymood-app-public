package pl.nubet.studymood.presentation.settings

import pl.nubet.studymood.domain.model.ReminderTime
import pl.nubet.studymood.domain.model.ThemePreference

sealed interface SettingsEvent {
    data object NavigateBack : SettingsEvent

    data object OpenProfile : SettingsEvent

    data object ShowEditNameDialog : SettingsEvent

    data class UpdateName(val name: String) : SettingsEvent

    data class ToggleDailyReminder(val enabled: Boolean) : SettingsEvent

    data object ShowReminderTimePicker : SettingsEvent

    data class SetReminderTime(val reminderTime: ReminderTime) : SettingsEvent

    data object OpenThemeSelector : SettingsEvent

    data class SetTheme(val theme: ThemePreference) : SettingsEvent

    data object ExportData : SettingsEvent

    data object ShowClearDataDialog : SettingsEvent

    data object ConfirmClearData : SettingsEvent

    data object OpenPrivacyPolicy : SettingsEvent

    data object SeedData : SettingsEvent

    data object ResetOnboarding : SettingsEvent
}
