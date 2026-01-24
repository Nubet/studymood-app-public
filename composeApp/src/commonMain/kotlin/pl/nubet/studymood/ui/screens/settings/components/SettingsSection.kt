package pl.nubet.studymood.ui.screens.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsSection(label: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 12.sp,
            letterSpacing = 0.16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
        )
        content()
    }
}
