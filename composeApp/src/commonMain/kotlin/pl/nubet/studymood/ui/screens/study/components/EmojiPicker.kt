package pl.nubet.studymood.ui.screens.study.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.domain.constants.StudyConstants
import pl.nubet.studymood.ui.components.interaction.pressEffect

@Composable
fun EmojiPicker(
    selectedEmoji: String,
    onEmojiSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        StudyConstants.DEFAULT_SUBJECT_EMOJIS.chunked(8).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                row.forEach { emoji ->
                    Box(
                        modifier =
                            Modifier.size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (emoji == selectedEmoji)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .border(
                                    width = 1.dp,
                                    color =
                                        if (emoji == selectedEmoji)
                                            MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(10.dp),
                                )
                                .clickable { onEmojiSelected(emoji) }
                                .pressEffect(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = emoji, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}
