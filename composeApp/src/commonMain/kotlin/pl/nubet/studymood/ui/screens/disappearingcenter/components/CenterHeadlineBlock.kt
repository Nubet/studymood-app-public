package pl.nubet.studymood.ui.screens.disappearingcenter.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CenterHeadlineBlock(mainText: String, helperText: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = mainText,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        if (helperText.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = helperText,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp,
            )
        }
    }
}
