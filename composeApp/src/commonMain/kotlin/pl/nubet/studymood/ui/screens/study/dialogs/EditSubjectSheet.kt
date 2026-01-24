package pl.nubet.studymood.ui.screens.study.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.domain.model.Subject
import pl.nubet.studymood.ui.screens.study.components.EmojiPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSubjectSheet(
    subject: Subject,
    onRename: (String) -> Unit,
    onChangeEmoji: (String) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    var showRenameDialog by remember { mutableStateOf(false) }
    var showEmojiDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            Text(
                text = "Edit subject",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 14.dp),
            )

            SheetOption(text = "Rename", onClick = { showRenameDialog = true })

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

            SheetOption(text = "Change emoji", onClick = { showEmojiDialog = true })

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

            SheetOption(
                text = "Delete subject",
                onClick = {
                    onDelete()
                    onDismiss()
                },
                color = Color(0xFFC0392B),
            )
        }
    }

    if (showRenameDialog) {
        RenameDialog(
            currentName = subject.name,
            onConfirm = { newName ->
                onRename(newName)
                showRenameDialog = false
                onDismiss()
            },
            onDismiss = { showRenameDialog = false },
        )
    }

    if (showEmojiDialog) {
        EmojiPickerDialog(
            currentEmoji = subject.emoji ?: "📘",
            onConfirm = { newEmoji ->
                onChangeEmoji(newEmoji)
                showEmojiDialog = false
                onDismiss()
            },
            onDismiss = { showEmojiDialog = false },
        )
    }
}

@Composable
private fun SheetOption(
    text: String,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    Box(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 12.dp)) {
        Text(text = text, fontSize = 16.sp, color = color)
    }
}

@Composable
private fun RenameDialog(currentName: String, onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Rename subject", fontWeight = FontWeight.SemiBold) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Enter new name...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    ),
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name) },
                enabled = name.isNotBlank(),
                colors =
                    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
            }
        },
    )
}

@Composable
private fun EmojiPickerDialog(
    currentEmoji: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedEmoji by remember { mutableStateOf(currentEmoji) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Choose emoji", fontWeight = FontWeight.SemiBold) },
        text = {
            EmojiPicker(selectedEmoji = selectedEmoji, onEmojiSelected = { selectedEmoji = it })
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedEmoji) },
                colors =
                    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
            }
        },
    )
}
