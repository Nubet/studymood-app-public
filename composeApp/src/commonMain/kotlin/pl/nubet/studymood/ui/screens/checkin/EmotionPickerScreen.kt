package pl.nubet.studymood.ui.screens.checkin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.domain.model.Emotion
import pl.nubet.studymood.ui.components.shape.EmotionShapeIcon
import pl.nubet.studymood.ui.theme.*

@Composable
fun EmotionPickerScreen(
    emotions: List<Emotion>,
    onEmotionSelect: (Emotion) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = LocalDimens.current
    val typography = LocalAppTypography.current

    var searchQuery by remember { mutableStateOf("") }
    var selectedQuadrant by remember { mutableStateOf<Int?>(null) }
    var expandedEmotionId by remember { mutableStateOf<String?>(null) }

    val filteredEmotions =
        remember(emotions, searchQuery, selectedQuadrant) {
            emotions.filter { emotion ->
                val matchesSearch =
                    emotion.label.contains(searchQuery, ignoreCase = true) ||
                        (emotion.synonym?.contains(searchQuery, ignoreCase = true) == true)
                val matchesQuadrant =
                    selectedQuadrant == null || emotion.quadrant == selectedQuadrant
                matchesSearch && matchesQuadrant
            }
        }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
    ) {
        Row(
            modifier =
                Modifier.fillMaxWidth().padding(horizontal = dimens.x16, vertical = dimens.x12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onDismiss) {
                Text(
                    text = "←",
                    style = typography.h2,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            Spacer(Modifier.width(dimens.x8))

            Text(
                text = "Choose emotion",
                style = typography.h2,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
            )
        }

        SearchBarTextField(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = dimens.x16),
        )

        Spacer(Modifier.height(dimens.x12))

        QuadrantFilterChips(
            selectedQuadrant = selectedQuadrant,
            onQuadrantSelect = { selectedQuadrant = it },
            modifier = Modifier.padding(horizontal = dimens.x16),
        )

        Spacer(Modifier.height(dimens.x16))

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = dimens.x16),
            verticalArrangement = Arrangement.spacedBy(dimens.x8),
        ) {
            items(filteredEmotions) { emotion ->
                EmotionPickerItem(
                    emotion = emotion,
                    isExpanded = expandedEmotionId == emotion.id,
                    onToggleExpand = {
                        expandedEmotionId =
                            if (expandedEmotionId == emotion.id) null else emotion.id
                    },
                    onClick = {
                        onEmotionSelect(emotion)
                        onDismiss()
                    },
                )
            }
        }
    }
}

@Composable
private fun SearchBarTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.clip(RoundedCornerShape(14.dp)),
        placeholder = {
            Text(
                text = "Search emotions...",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 15.sp,
            )
        },
        colors =
            TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontSize = 15.sp, fontWeight = FontWeight.Medium),
    )
}

@Composable
private fun QuadrantFilterChips(
    selectedQuadrant: Int?,
    onQuadrantSelect: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            FilterChip(
                label = "All",
                isSelected = selectedQuadrant == null,
                color = MaterialTheme.colorScheme.outline,
                onClick = { onQuadrantSelect(null) },
            )
        }

        item {
            FilterChip(
                label = "Yellow",
                isSelected = selectedQuadrant == 1,
                color = Color(0xFFFAD900),
                onClick = { onQuadrantSelect(1) },
            )
        }

        item {
            FilterChip(
                label = "Red",
                isSelected = selectedQuadrant == 2,
                color = Color(0xFFFF006E),
                onClick = { onQuadrantSelect(2) },
            )
        }

        item {
            FilterChip(
                label = "Blue",
                isSelected = selectedQuadrant == 3,
                color = Color(0xFFAA90BB),
                onClick = { onQuadrantSelect(3) },
            )
        }

        item {
            FilterChip(
                label = "Green",
                isSelected = selectedQuadrant == 4,
                color = Color(0xFF21B58F),
                onClick = { onQuadrantSelect(4) },
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSelected) color else MaterialTheme.colorScheme.surface
    val textColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(percent = 50))
                .background(backgroundColor)
                .clickable(onClick = onClick)
                .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor,
        )
    }
}

@Composable
private fun EmotionPickerItem(
    emotion: Emotion,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = LocalDimens.current
    val typography = LocalAppTypography.current
    val hasDescription = !emotion.description.isNullOrBlank()

    Column(
        modifier =
            modifier
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surface)
                .animateContentSize(animationSpec = tween(durationMillis = 300))
    ) {
        Row(
            modifier =
                Modifier.clickable(onClick = onClick)
                    .padding(horizontal = dimens.x16, vertical = dimens.x12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            EmotionShapeIcon(emotion = emotion, size = 40.dp)

            Spacer(Modifier.width(dimens.x16))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = emotion.label.replaceFirstChar { it.uppercase() },
                        style = typography.body1,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    if (hasDescription) {
                        Spacer(Modifier.width(dimens.x8))
                        IconButton(onClick = onToggleExpand, modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "Show description",
                                modifier = Modifier.size(16.dp),
                                tint =
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                        alpha = if (isExpanded) 0.6f else 0.38f
                                    ),
                            )
                        }
                    }
                }

                if (emotion.synonym != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = emotion.synonym,
                        style = typography.caption,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Box(
                modifier =
                    Modifier.size(8.dp)
                        .clip(CircleShape)
                        .background(EmotionColors.forQuadrant(emotion.quadrant).copy(alpha = 0.4f))
            )
        }

        AnimatedVisibility(
            visible = isExpanded && hasDescription,
            enter =
                fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
            exit =
                fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300)),
        ) {
            Box(
                modifier =
                    Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                        .padding(horizontal = dimens.x16, vertical = dimens.x12)
            ) {
                Text(
                    text = emotion.description ?: "",
                    style = typography.caption,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp,
                )
            }
        }
    }
}
