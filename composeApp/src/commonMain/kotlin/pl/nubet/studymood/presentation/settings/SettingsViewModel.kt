package pl.nubet.studymood.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.nubet.studymood.core.logging.Log
import pl.nubet.studymood.data.repository.MoodRepository
import pl.nubet.studymood.data.repository.OnboardingRepository
import pl.nubet.studymood.data.repository.StudyRepository
import pl.nubet.studymood.data.repository.SubjectRepository
import pl.nubet.studymood.domain.usecase.ExportUserData

class SettingsViewModel(
    private val onboardingRepo: OnboardingRepository,
    private val moodRepo: MoodRepository,
    private val studyRepo: StudyRepository,
    private val subjectRepo: SubjectRepository,
    private val exportUserData: ExportUserData,
    private val debugDataSeeder: pl.nubet.studymood.data.debug.DebugDataSeeder,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state = _state.asStateFlow()

    private val _showClearDataDialog = MutableStateFlow(false)
    val showClearDataDialog = _showClearDataDialog.asStateFlow()

    private val _showEditNameDialog = MutableStateFlow(false)
    val showEditNameDialog = _showEditNameDialog.asStateFlow()

    private val _showReminderTimeDialog = MutableStateFlow(false)
    val showReminderTimeDialog = _showReminderTimeDialog.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            try {
                val reminderEnabled = onboardingRepo.getDailyReminderEnabled()
                val userName = onboardingRepo.getUserName()
                val theme = onboardingRepo.getThemePreference()
                val reminderTime = onboardingRepo.getReminderTime()

                _state.update {
                    it.copy(
                        dailyReminderEnabled = reminderEnabled,
                        profileName = userName.ifEmpty { "User" },
                        theme = theme,
                        reminderTime = reminderTime,
                    )
                }
            } catch (e: Exception) {
                Log.e("Error loading settings: ${e.message}", e, tag = "SettingsViewModel")
            }
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ShowEditNameDialog -> {
                _showEditNameDialog.value = true
            }
            is SettingsEvent.UpdateName -> {
                _showEditNameDialog.value = false
                val trimmedName = event.name.trim()
                if (trimmedName.isNotEmpty()) {
                    _state.update { it.copy(profileName = trimmedName) }
                    viewModelScope.launch {
                        try {
                            onboardingRepo.setUserName(trimmedName)
                            Log.i("Name updated to: $trimmedName", tag = "SettingsViewModel")
                        } catch (e: Exception) {
                            Log.e("Error updating name: ${e.message}", e, tag = "SettingsViewModel")
                        }
                    }
                }
            }
            is SettingsEvent.ToggleDailyReminder -> {
                _state.update { it.copy(dailyReminderEnabled = event.enabled) }
                viewModelScope.launch {
                    try {
                        onboardingRepo.setDailyReminderEnabled(event.enabled)
                        Log.i(
                            "Daily reminder ${if (event.enabled) "enabled" else "disabled"}",
                            tag = "SettingsViewModel",
                        )
                    } catch (e: Exception) {
                        Log.e(
                            "Error saving daily reminder: ${e.message}",
                            e,
                            tag = "SettingsViewModel",
                        )
                    }
                }
            }
            is SettingsEvent.ShowReminderTimePicker -> {
                _showReminderTimeDialog.value = true
            }
            is SettingsEvent.SetReminderTime -> {
                _showReminderTimeDialog.value = false
                _state.update { it.copy(reminderTime = event.reminderTime) }
                viewModelScope.launch {
                    try {
                        onboardingRepo.setReminderTime(event.reminderTime)
                        val shouldEnableReminder =
                            event.reminderTime != pl.nubet.studymood.domain.model.ReminderTime.NONE
                        onboardingRepo.setDailyReminderEnabled(shouldEnableReminder)
                        _state.update { it.copy(dailyReminderEnabled = shouldEnableReminder) }
                        Log.i(
                            "Reminder time set to: ${event.reminderTime.displayName}",
                            tag = "SettingsViewModel",
                        )
                    } catch (e: Exception) {
                        Log.e(
                            "Error saving reminder time: ${e.message}",
                            e,
                            tag = "SettingsViewModel",
                        )
                    }
                }
            }
            is SettingsEvent.SetTheme -> {
                _state.update { it.copy(theme = event.theme) }
                viewModelScope.launch {
                    try {
                        onboardingRepo.setThemePreference(event.theme)
                        Log.i("Theme set to ${event.theme.name}", tag = "SettingsViewModel")
                    } catch (e: Exception) {
                        Log.e("Error saving theme: ${e.message}", e, tag = "SettingsViewModel")
                    }
                }
            }
            is SettingsEvent.ShowClearDataDialog -> {
                _showClearDataDialog.value = true
            }
            is SettingsEvent.ExportData -> {
                viewModelScope.launch {
                    try {
                        val jsonData = exportUserData()

                        _state.update { it.copy(exportedData = jsonData) }
                        Log.i(
                            "Data exported successfully (${jsonData.length} characters)",
                            tag = "SettingsViewModel",
                        )
                    } catch (e: Exception) {
                        Log.e("Error exporting data: ${e.message}", e, tag = "SettingsViewModel")
                    }
                }
            }
            is SettingsEvent.ConfirmClearData -> {
                _showClearDataDialog.value = false
                viewModelScope.launch {
                    try {
                        moodRepo.deleteAll()
                        studyRepo.deleteAll()
                        subjectRepo.deleteAll()
                        Log.i("All data cleared successfully", tag = "SettingsViewModel")
                    } catch (e: Exception) {
                        Log.e("Error clearing data: ${e.message}", e, tag = "SettingsViewModel")
                    }
                }
            }
            is SettingsEvent.ResetOnboarding -> {
                viewModelScope.launch {
                    try {
                        onboardingRepo.resetOnboarding()
                        Log.i("Onboarding reset successfully", tag = "SettingsViewModel")
                    } catch (e: Exception) {
                        Log.e(
                            "Error resetting onboarding: ${e.message}",
                            e,
                            tag = "SettingsViewModel",
                        )
                    }
                }
            }
            is SettingsEvent.OpenPrivacyPolicy -> {
                try {
                    pl.nubet.studymood.core.openUrl("https://github.com/Nubet/studymood-app-public")
                    Log.i("Opening Privacy Policy URL", tag = "SettingsViewModel")
                } catch (e: Exception) {
                    Log.e(
                        "Error opening Privacy Policy: ${e.message}",
                        e,
                        tag = "SettingsViewModel",
                    )
                }
            }
            is SettingsEvent.SeedData -> {
                viewModelScope.launch {
                    try {
                        debugDataSeeder.seedIfNeeded()
                        Log.i("Data seeded successfully", tag = "SettingsViewModel")
                    } catch (e: Exception) {
                        Log.e("Error seeding data: ${e.message}", e, tag = "SettingsViewModel")
                    }
                }
            }
            else -> {}
        }
    }

    fun dismissClearDataDialog() {
        _showClearDataDialog.value = false
    }

    fun dismissEditNameDialog() {
        _showEditNameDialog.value = false
    }

    fun dismissReminderTimeDialog() {
        _showReminderTimeDialog.value = false
    }

    fun clearExportedData() {
        _state.update { it.copy(exportedData = null) }
    }
}
