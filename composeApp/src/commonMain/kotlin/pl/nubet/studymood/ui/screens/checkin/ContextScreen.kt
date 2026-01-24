package pl.nubet.studymood.ui.screens.checkin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.domain.constants.CheckInConstants
import pl.nubet.studymood.ui.components.interaction.pressEffect
import pl.nubet.studymood.ui.theme.LocalAppTypography
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun ContextScreen(
    emotionLabel: String,
    onComplete: (activity: String, companion: String?, location: String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = LocalDimens.current
    val scrollState = rememberScrollState()

    var activityText by remember { mutableStateOf("") }
    var selectedActivity by remember { mutableStateOf<String?>(null) }
    var selectedCompanion by remember { mutableStateOf<String?>(null) }
    var selectedLocation by remember { mutableStateOf<String?>(null) }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(horizontal = dimens.x20, vertical = dimens.x24),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(dimens.x24))

        Text(
            text = "What's going on?",
            style = LocalAppTypography.current.h1.copy(fontSize = 34.sp),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )
        if (emotionLabel.isNotBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Feeling: $emotionLabel",
                style = LocalAppTypography.current.caption,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(Modifier.height(dimens.x24))

        SectionTitle(text = "What are you doing?")
        Spacer(Modifier.height(dimens.x12))

        OutlinedTextField(
            value = activityText,
            onValueChange = { activityText = it },
            placeholder = {
                Text(
                    "Describe it briefly…",
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                ),
            modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(20.dp)),
        )

        Spacer(Modifier.height(dimens.x24))

        SectionTitle(text = "Who are you with?")
        Spacer(Modifier.height(dimens.x12))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AddChip(onClick = {})

            CheckInConstants.DEFAULT_COMPANIONS.forEach { companion ->
                CompanionChip(
                    label = companion,
                    selected = selectedCompanion == companion,
                    onClick = {
                        selectedCompanion = if (selectedCompanion == companion) null else companion
                    },
                )
            }
        }

        Spacer(Modifier.height(dimens.x24))

        SectionTitle(text = "Where are you?")
        Spacer(Modifier.height(dimens.x12))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AddChip(onClick = {})

            CheckInConstants.DEFAULT_LOCATIONS.forEach { location ->
                LocationChip(
                    label = location,
                    selected = selectedLocation == location,
                    onClick = {
                        selectedLocation = if (selectedLocation == location) null else location
                    },
                )
            }
        }

        Spacer(Modifier.height(dimens.x24))

        CompleteCheckInButton(
            onClick = {
                val finalActivity = activityText.ifBlank { selectedActivity } ?: ""
                onComplete(finalActivity, selectedCompanion, selectedLocation)
            }
        )

        Spacer(Modifier.height(dimens.x24))
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = LocalAppTypography.current.h3,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun AddChip(onClick: () -> Unit) {
    Box(
        modifier =
            Modifier.size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFF3D9))
                .border(0.dp, Color.Transparent, CircleShape)
                .pressEffect(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add custom",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun ActivityChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val bgColor =
        if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant
    val textColor =
        if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Box(
        modifier =
            Modifier.clip(RoundedCornerShape(24.dp))
                .background(bgColor)
                .pressEffect(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            style = LocalAppTypography.current.body2,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun CompanionChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val bgColor =
        if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.tertiaryContainer
    val textColor =
        if (selected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onTertiaryContainer

    Box(
        modifier =
            Modifier.clip(RoundedCornerShape(24.dp))
                .background(bgColor)
                .pressEffect(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            style = LocalAppTypography.current.body2,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun LocationChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val bgColor =
        if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.secondaryContainer
    val textColor =
        if (selected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSecondaryContainer

    Box(
        modifier =
            Modifier.clip(RoundedCornerShape(24.dp))
                .background(bgColor)
                .pressEffect(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            style = LocalAppTypography.current.body2,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun CompleteCheckInButton(onClick: () -> Unit) {
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(56.dp)
                .shadow(4.dp, RoundedCornerShape(28.dp))
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.primary)
                .pressEffect(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Complete check-in",
            style = LocalAppTypography.current.h3.copy(fontSize = 18.sp),
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
        )
    }
}
