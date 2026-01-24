package pl.nubet.studymood.ui.components.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pl.nubet.studymood.ui.theme.LocalAppShapes

@Composable
fun AppDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
    confirmButton: @Composable () -> Unit,
    dismissButton: (@Composable () -> Unit)? = null,
) {
    val shape = LocalAppShapes.current.dialog
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(initialScale = 0.96f),
        exit = fadeOut() + scaleOut(targetScale = 0.96f),
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            modifier = modifier,
            title = title,
            text = text,
            confirmButton = confirmButton,
            dismissButton = dismissButton,
            shape = shape,
            containerColor = MaterialTheme.colorScheme.surface,
        )
    }
}
