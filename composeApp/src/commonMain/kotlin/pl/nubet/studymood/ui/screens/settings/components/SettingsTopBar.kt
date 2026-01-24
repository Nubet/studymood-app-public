package pl.nubet.studymood.ui.screens.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsTopBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().height(44.dp).padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Settings",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
