package pl.nubet.studymood.ui.screens.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp

@Composable
fun EditNameDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var nameText by remember { mutableStateOf(currentName) }
    val isValid = nameText.trim().isNotEmpty()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit name", fontWeight = FontWeight.SemiBold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "This name will be shown in your profile and throughout the app.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nameText,
                    onValueChange = { nameText = it },
                    label = { Text("Name") },
                    singleLine = true,
                    keyboardOptions =
                        KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Done,
                        ),
                    keyboardActions =
                        KeyboardActions(
                            onDone = {
                                if (isValid) {
                                    onConfirm(nameText)
                                }
                            }
                        ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(nameText) }, enabled = isValid) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        modifier = modifier,
    )
}
