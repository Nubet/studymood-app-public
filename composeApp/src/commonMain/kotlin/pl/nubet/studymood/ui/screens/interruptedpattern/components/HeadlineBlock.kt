package pl.nubet.studymood.ui.screens.interruptedpattern.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeadlineBlock(mainText: String, helperText: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = mainText,
            style =
                MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
        )

        if (helperText.isNotEmpty()) {
            Text(
                text = helperText,
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
            )
        }
    }
}
