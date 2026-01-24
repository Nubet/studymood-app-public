package pl.nubet.studymood.ui.screens.study.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import pl.nubet.studymood.ui.screens.study.components.EmojiPicker

@Composable
fun AddSubjectDialog(
    name: String,
    emoji: String,
    onNameChange: (String) -> Unit,
    onEmojiChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier =
                Modifier.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = "Add new subject",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                placeholder = {
                    Text(
                        "Enter subject name...",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    ),
            )

            Text(
                text = "Choose emoji:",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )

            EmojiPicker(selectedEmoji = emoji, onEmojiSelected = onEmojiChange)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White,
                        ),
                    enabled = name.isNotBlank(),
                ) {
                    Text(text = "Save", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
