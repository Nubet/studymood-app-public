package pl.nubet.studymood.ui.screens.study

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import pl.nubet.studymood.presentation.study.StudyUiEvent
import pl.nubet.studymood.presentation.study.StudyViewModel
import pl.nubet.studymood.ui.components.PrimaryButton
import pl.nubet.studymood.ui.screens.study.components.*
import pl.nubet.studymood.ui.screens.study.dialogs.AddSubjectDialog
import pl.nubet.studymood.ui.screens.study.dialogs.EditSubjectSheet
import pl.nubet.studymood.ui.theme.LocalAppTypography
import pl.nubet.studymood.ui.theme.LocalDimens

@Composable
fun StudyScreen(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(),
    onNavigateToCheckIn: () -> Unit = {},
) {
    val vm: StudyViewModel = koinInject()

    val state by vm.state.collectAsState()

    if (state.showSessionScreen && state.sessionSubject != null) {
        StudySessionScreen(
            subject = state.sessionSubject!!,
            onNavigateToCheckIn = {
                vm.onEvent(StudyUiEvent.CloseSessionScreen)
                onNavigateToCheckIn()
            },
            onNavigateToHome = { vm.onEvent(StudyUiEvent.CloseSessionScreen) },
            modifier = modifier,
        )
        return
    }

    StudyContent(state = state, onEvent = vm::onEvent, modifier = modifier, padding = padding)
}

@Composable
private fun StudyContent(
    state: pl.nubet.studymood.presentation.study.StudyUiState,
    onEvent: (StudyUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(),
) {
    val dimens = LocalDimens.current
    val typography = LocalAppTypography.current

    val sectionGap = dimens.x16 * 0.75f

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = dimens.x16)
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(sectionGap),
    ) {
        Spacer(modifier = Modifier.height(dimens.x8))

        StudyHeader(timeOfDayIcon = state.timeOfDayIcon)

        Text(
            text = state.microcopy,
            style = typography.caption,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = dimens.x8),
        )

        SubjectsHeader(onAddClick = { onEvent(StudyUiEvent.AddSubjectClicked) })

        Text(
            text = "Choose what to focus on",
            style = typography.caption,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (state.subjects.isEmpty()) {
            EmptySubjectsPlaceholder(typography = typography, dimens = dimens)
        } else {
            SubjectsList(
                subjects = state.subjects,
                selectedSubjectId = state.selectedSubjectId,
                onSubjectClick = { id -> onEvent(StudyUiEvent.SelectSubject(id)) },
                onSubjectLongPress = { id -> onEvent(StudyUiEvent.SubjectCardLongPressed(id)) },
            )
        }

        StartSessionButton(
            enabled = state.selectedSubjectId != null,
            onClick = { onEvent(StudyUiEvent.Start) },
        )

        if (state.selectedSubjectId == null && state.subjects.isNotEmpty()) {
            SelectSubjectHint(typography = typography)
        }

        if (state.recentSessions.isNotEmpty()) {
            RecentStudyTimeline(sessions = state.recentSessions, modifier = Modifier.fillMaxWidth())
        }
    }

    if (state.showAddDialog) {
        AddSubjectDialog(
            name = state.addSubjectName,
            emoji = state.addSubjectEmoji,
            onNameChange = { onEvent(StudyUiEvent.SubjectNameChanged(it)) },
            onEmojiChange = { onEvent(StudyUiEvent.SubjectEmojiChanged(it)) },
            onConfirm = { onEvent(StudyUiEvent.ConfirmAddSubject) },
            onDismiss = { onEvent(StudyUiEvent.DismissAddSubjectDialog) },
        )
    }

    if (state.showEditSheet && state.subjectBeingEdited != null) {
        EditSubjectSheet(
            subject = state.subjectBeingEdited,
            onRename = { onEvent(StudyUiEvent.RenameSubject(it)) },
            onChangeEmoji = { onEvent(StudyUiEvent.ChangeSubjectEmoji(it)) },
            onDelete = { onEvent(StudyUiEvent.DeleteSubject) },
            onDismiss = { onEvent(StudyUiEvent.DismissEditSheet) },
        )
    }
}

@Composable
private fun EmptySubjectsPlaceholder(
    typography: pl.nubet.studymood.ui.theme.AppTypography,
    dimens: pl.nubet.studymood.ui.theme.Dimens,
) {
    Text(
        text = "No subjects yet. Add your first subject to get started.",
        style = typography.body2,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        modifier = Modifier.padding(vertical = dimens.x20),
    )
}

@Composable
private fun StartSessionButton(enabled: Boolean, onClick: () -> Unit) {
    PrimaryButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 15.dp),
    ) {
        Text("Start Study Session")
    }
}

@Composable
private fun SelectSubjectHint(typography: pl.nubet.studymood.ui.theme.AppTypography) {
    Text(
        text = "Select a subject to start studying",
        style = typography.caption,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
}
