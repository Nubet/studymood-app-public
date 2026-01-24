package pl.nubet.studymood.presentation.settings

import pl.nubet.studymood.domain.model.ReminderTime
import pl.nubet.studymood.domain.model.ThemePreference

data class SettingsUiState(
    val profileName: String = "Name",
    val dailyReminderEnabled: Boolean = true,
    val reminderTime: ReminderTime = ReminderTime.NONE,
    val theme: ThemePreference = ThemePreference.LIGHT,
    val exportedData: String? = null,
    val dialogs: DialogState = DialogState(),
)

data class DialogState(
    val editName: Boolean = false,
    val reminderTime: Boolean = false,
    val clearData: Boolean = false,
)
