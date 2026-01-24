package pl.nubet.studymood.presentation.study

sealed class StudyUiEvent {
    data class SelectSubject(val subjectId: String) : StudyUiEvent()

    data class SubjectCardLongPressed(val subjectId: String) : StudyUiEvent()

    data object Start : StudyUiEvent()

    data object Pause : StudyUiEvent()

    data object Resume : StudyUiEvent()

    data object Stop : StudyUiEvent()

    data object StartStudySessionClicked : StudyUiEvent()

    data class UpdateMiniMood(val pleasant: Int? = null, val energy: Int? = null) : StudyUiEvent()

    data object SaveSession : StudyUiEvent()

    data object DismissMiniMood : StudyUiEvent()

    data object AddSubjectClicked : StudyUiEvent()

    data class SubjectNameChanged(val name: String) : StudyUiEvent()

    data class SubjectEmojiChanged(val emoji: String) : StudyUiEvent()

    data object ConfirmAddSubject : StudyUiEvent()

    data object DismissAddSubjectDialog : StudyUiEvent()

    data object DismissEditSheet : StudyUiEvent()

    data class RenameSubject(val newName: String) : StudyUiEvent()

    data class ChangeSubjectEmoji(val newEmoji: String) : StudyUiEvent()

    data object DeleteSubject : StudyUiEvent()

    data object CloseSessionScreen : StudyUiEvent()

    data object RefreshData : StudyUiEvent()

    @Deprecated("Use AddSubjectClicked instead")
    data class ShowAddDialog(val show: Boolean) : StudyUiEvent()

    @Deprecated("Use ConfirmAddSubject instead")
    data class AddSubject(val name: String, val emoji: String?, val color: String?) :
        StudyUiEvent()
}
