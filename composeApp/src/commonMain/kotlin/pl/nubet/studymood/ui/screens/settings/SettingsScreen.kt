package pl.nubet.studymood.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import pl.nubet.studymood.core.logging.Log
import pl.nubet.studymood.presentation.settings.SettingsEvent
import pl.nubet.studymood.presentation.settings.SettingsViewModel
import pl.nubet.studymood.ui.screens.settings.components.*

@Composable
fun SettingsScreen(modifier: Modifier = Modifier, onResetOnboarding: () -> Unit = {}) {
    val viewModel: SettingsViewModel = koinInject()

    val state by viewModel.state.collectAsState()
    val showClearDataDialog by viewModel.showClearDataDialog.collectAsState()
    val showEditNameDialog by viewModel.showEditNameDialog.collectAsState()
    val showReminderTimeDialog by viewModel.showReminderTimeDialog.collectAsState()
    val scope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            SettingsTopBar(modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp))

            Column(
                modifier =
                    Modifier.fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                SettingsSection(label = "ACCOUNT") {
                    SettingsGroup {
                        SettingsNavigationRow(
                            title = "Profile",
                            subtitle = "Name, avatar, basic info",
                            value = state.profileName,
                            onClick = { viewModel.onEvent(SettingsEvent.ShowEditNameDialog) },
                        )
                    }
                }

                SettingsSection(label = "CHECK-IN") {
                    SettingsGroup {
                        SettingsToggleRow(
                            title = "Daily reminder",
                            subtitle = "Gentle nudge to log how you feel",
                            checked = state.dailyReminderEnabled,
                            onCheckedChange = { enabled ->
                                viewModel.onEvent(SettingsEvent.ToggleDailyReminder(enabled))
                            },
                        )
                        SettingsRowDivider()
                        SettingsNavigationRow(
                            title = "Reminder time",
                            subtitle = "When to nudge you",
                            value = state.reminderTime.displayName,
                            onClick = { viewModel.onEvent(SettingsEvent.ShowReminderTimePicker) },
                        )
                    }
                }

                SettingsSection(label = "APPEARANCE") {
                    SettingsGroup {
                        SettingsNavigationRow(
                            title = "Theme",
                            subtitle = "Light or dark mode",
                            value =
                                when (state.theme) {
                                    pl.nubet.studymood.domain.model.ThemePreference.LIGHT -> "Light"
                                    pl.nubet.studymood.domain.model.ThemePreference.DARK -> "Dark"
                                    pl.nubet.studymood.domain.model.ThemePreference.SYSTEM ->
                                        "System"
                                },
                            onClick = {
                                val newTheme =
                                    if (
                                        state.theme ==
                                            pl.nubet.studymood.domain.model.ThemePreference.LIGHT
                                    ) {
                                        pl.nubet.studymood.domain.model.ThemePreference.DARK
                                    } else {
                                        pl.nubet.studymood.domain.model.ThemePreference.LIGHT
                                    }
                                viewModel.onEvent(SettingsEvent.SetTheme(newTheme))
                            },
                        )
                    }
                }

                SettingsSection(label = "DATA & PRIVACY") {
                    SettingsGroup {
                        SettingsNavigationRow(
                            title = "Export data",
                            subtitle = "Download your moods and study history",
                            onClick = { viewModel.onEvent(SettingsEvent.ExportData) },
                        )
                        SettingsRowDivider()
                        SettingsNavigationRow(
                            title = "Clear data",
                            subtitle = "Remove all entries from this device",
                            isDanger = true,
                            onClick = { viewModel.onEvent(SettingsEvent.ShowClearDataDialog) },
                        )
                        SettingsRowDivider()
                        SettingsNavigationRow(
                            title = "Privacy Policy",
                            onClick = { viewModel.onEvent(SettingsEvent.OpenPrivacyPolicy) },
                        )
                    }
                }

                SettingsSection(label = "DEV") {
                    SettingsGroup {
                        SettingsNavigationRow(
                            title = "seed data",
                            subtitle = "Fill app with demo moods and sessions",
                            onClick = { scope.launch { viewModel.onEvent(SettingsEvent.SeedData) } },
                        )

                        SettingsRowDivider()
                        SettingsNavigationRow(
                            title = "reset onboarding",
                            subtitle = "Show intro flow again on next launch",
                            onClick = {
                                scope.launch {
                                    viewModel.onEvent(SettingsEvent.ResetOnboarding)
                                    onResetOnboarding()
                                }
                            },
                        )
                    }
                }

                Text(
                    text = "Your data stays on this device unless you export it.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
                )
            }
        }
    }

    if (showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissClearDataDialog() },
            title = { Text(text = "Clear all data?", fontWeight = FontWeight.SemiBold) },
            text = {
                Text(
                    "This will permanently delete all your mood check-ins, study sessions, and settings. This action cannot be undone."
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(SettingsEvent.ConfirmClearData) }) {
                    Text("Clear", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissClearDataDialog() }) { Text("Cancel") }
            },
        )
    }

    if (showEditNameDialog) {
        EditNameDialog(
            currentName = state.profileName,
            onDismiss = { viewModel.dismissEditNameDialog() },
            onConfirm = { newName -> viewModel.onEvent(SettingsEvent.UpdateName(newName)) },
        )
    }

    if (showReminderTimeDialog) {
        ReminderTimePickerDialog(
            currentTime = state.reminderTime,
            onDismiss = { viewModel.dismissReminderTimeDialog() },
            onConfirm = { newTime -> viewModel.onEvent(SettingsEvent.SetReminderTime(newTime)) },
        )
    }

    state.exportedData?.let { exportedData ->
        AlertDialog(
            onDismissRequest = { viewModel.clearExportedData() },
            title = { Text(text = "Data exported", fontWeight = FontWeight.SemiBold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Your data has been exported to JSON format.")
                    Text(
                        text = "${exportedData.length} characters",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            try {
                                Log.i("Exported data copied to clipboard", tag = "SettingsScreen")
                                Log.d(exportedData, tag = "SettingsScreen")
                                viewModel.clearExportedData()
                            } catch (e: Exception) {
                                Log.e(
                                    "Error copying to clipboard: ${e.message}",
                                    e,
                                    tag = "SettingsScreen",
                                )
                            }
                        }
                    }
                ) {
                    Text("Copy to clipboard")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.clearExportedData() }) { Text("Close") }
            },
        )
    }
}
