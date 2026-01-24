package pl.nubet.studymood.ui.screens.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsNavigationRow(
    title: String,
    subtitle: String? = null,
    value: String? = null,
    isDanger: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = title,
                fontSize = 14.sp,
                color =
                    if (isDanger) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (value != null) {
                Text(
                    text = value,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = "›",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f),
            )
        }
    }
}

@Composable
fun SettingsRowDivider() {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant,
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 14.dp),
    )
}
