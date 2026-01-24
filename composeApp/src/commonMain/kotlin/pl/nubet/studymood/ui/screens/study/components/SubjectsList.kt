package pl.nubet.studymood.ui.screens.study.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.nubet.studymood.domain.model.SubjectWithSessionCount

@Composable
fun SubjectsList(
    subjects: List<SubjectWithSessionCount>,
    selectedSubjectId: String?,
    onSubjectClick: (String) -> Unit,
    onSubjectLongPress: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(subjects, key = { it.subject.id }) { subjectWithCount ->
            SubjectCard(
                subject = subjectWithCount.subject,
                sessionCount = subjectWithCount.sessionCount,
                isSelected = subjectWithCount.subject.id == selectedSubjectId,
                onClick = { onSubjectClick(subjectWithCount.subject.id) },
                onLongPress = { onSubjectLongPress(subjectWithCount.subject.id) },
            )
        }
    }
}
