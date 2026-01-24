package pl.nubet.studymood.ui.screens.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pl.nubet.studymood.domain.model.ReminderTime

@Composable
fun ReminderTimePickerDialog(
    currentTime: ReminderTime,
    onDismiss: () -> Unit,
    onConfirm: (ReminderTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTime by remember { mutableStateOf(currentTime) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Reminder time", fontWeight = FontWeight.SemiBold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Choose when you'd like to be reminded to check in.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(8.dp))

                ReminderTime.entries.forEach { time ->
                    ReminderTimeOption(
                        reminderTime = time,
                        isSelected = selectedTime == time,
                        onClick = { selectedTime = time },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
        confirmButton = { TextButton(onClick = { onConfirm(selectedTime) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        modifier = modifier,
    )
}

@Composable
private fun ReminderTimeOption(
    reminderTime: ReminderTime,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.selectable(selected = isSelected, onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color =
            if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
        border =
            androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color =
                    if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    },
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = reminderTime.displayName,
                style = MaterialTheme.typography.bodyLarge,
                color =
                    if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            )

            RadioButton(
                selected = isSelected,
                onClick = null,
                colors =
                    RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary),
            )
        }
    }
}
