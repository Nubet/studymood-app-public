package pl.nubet.studymood.ui.screens.quotes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun QuotesBottomActions(
    isSaved: Boolean,
    isCopyFeedbackActive: Boolean,
    onSaveClick: () -> Unit,
    onCopyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = LocalDimens.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimens.x24, Alignment.CenterHorizontally),
    ) {
        IconButton(
            onClick = onSaveClick,
            modifier =
                Modifier.size(52.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = CircleShape,
                        ambientColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
                    )
                    .background(
                        if (isSaved) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                        CircleShape,
                    )
                    .border(
                        1.dp,
                        if (isSaved) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        } else {
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                        },
                        CircleShape,
                    ),
        ) {
            Icon(
                imageVector =
                    if (isSaved) {
                        Icons.Filled.Bookmark
                    } else {
                        Icons.Outlined.BookmarkBorder
                    },
                contentDescription = if (isSaved) "Unsave quote" else "Save quote",
                tint =
                    if (isSaved) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                modifier = Modifier.size(20.dp),
            )
        }

        IconButton(
            onClick = onCopyClick,
            modifier =
                Modifier.size(52.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = CircleShape,
                        ambientColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
                    )
                    .background(
                        if (isCopyFeedbackActive) {
                            MaterialTheme.colorScheme.tertiaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                        CircleShape,
                    )
                    .border(
                        1.dp,
                        if (isCopyFeedbackActive) {
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                        } else {
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                        },
                        CircleShape,
                    ),
        ) {
            Icon(
                imageVector = Icons.Outlined.ContentCopy,
                contentDescription = "Copy quote",
                tint =
                    if (isCopyFeedbackActive) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
