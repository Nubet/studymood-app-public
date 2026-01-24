package pl.nubet.studymood.domain.model

import org.jetbrains.compose.resources.DrawableResource
import studymood.composeapp.generated.resources.*

sealed class MindToolCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconRes: DrawableResource,
    val tag: String,
    val exerciseCount: Int,
) {
    data object Breathing :
        MindToolCategory(
            id = "breathing",
            title = "Breathing",
            subtitle = "Fast calm for stress and restlessness.",
            iconRes = Res.drawable.breathing,
            tag = "Body first",
            exerciseCount = 2,
        )

    data object Reframing :
        MindToolCategory(
            id = "reframing",
            title = "Reframing",
            subtitle = "Guided shifts for self doubt and overthinking.",
            iconRes = Res.drawable.reframing,
            tag = "Guided",
            exerciseCount = 2,
        )

    data object InteractiveActions :
        MindToolCategory(
            id = "interactive_actions",
            title = "Interactive Actions",
            subtitle = "Short exercises that redirect your attention.",
            iconRes = Res.drawable.grounding,
            tag = "1–2 min",
            exerciseCount = 3,
        )

    data object Quotes :
        MindToolCategory(
            id = "quotes",
            title = "Quotes",
            subtitle = "Tiny prompts that interrupt spirals.",
            iconRes = Res.drawable.quotes,
            tag = "Light",
            exerciseCount = 40,
        )

    companion object {
        fun all() = listOf(Breathing, Reframing, InteractiveActions, Quotes)

        fun fromId(id: String) = all().find { it.id == id }
    }
}
