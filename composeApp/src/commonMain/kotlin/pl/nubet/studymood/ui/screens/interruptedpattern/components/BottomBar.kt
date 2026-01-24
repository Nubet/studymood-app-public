package pl.nubet.studymood.ui.screens.interruptedpattern.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.nubet.studymood.ui.components.interaction.pressEffect

@Composable
fun BottomBar(showDoneButton: Boolean, onDoneClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedVisibility(
            visible = showDoneButton,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally(),
        ) {
            Button(
                onClick = onDoneClick,
                modifier = Modifier.pressEffect().height(44.dp),
                shape = RoundedCornerShape(999.dp),
                colors =
                    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 11.dp),
            ) {
                Text(
                    text = "Done",
                    style =
                        MaterialTheme.typography.labelLarge.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimary,
                        ),
                )
            }
        }
    }
}
