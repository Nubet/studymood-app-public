package pl.nubet.studymood.domain.usecase

import pl.nubet.studymood.domain.model.MindToolExercise

class GetExercisesByCategory {
    operator fun invoke(categoryId: String): List<MindToolExercise> {
        return when (categoryId) {
            "breathing" ->
                listOf(
                    MindToolExercise(
                        id = "box_breathing",
                        categoryId = "breathing",
                        title = "Box breathing",
                        description = "Inhale, hold, exhale, hold.",
                        durationMinutes = 2,
                        requiresFullScreen = true,
                    ),
                    MindToolExercise(
                        id = "478_breathing",
                        categoryId = "breathing",
                        title = "4-7-8",
                        description = "A classic wind down pattern.",
                        durationMinutes = 2,
                        requiresFullScreen = true,
                    ),
                )
            "reframing" ->
                listOf(
                    MindToolExercise(
                        id = "self_talk",
                        categoryId = "reframing",
                        title = "Self Talk",
                        description = "Speak to yourself like someone you love.",
                        durationMinutes = 2,
                    ),
                    MindToolExercise(
                        id = "bright_spots",
                        categoryId = "reframing",
                        title = "Bright Spots",
                        description = "A gentle body scan and gratitude practice.",
                        durationMinutes = 2,
                    ),
                )
            "interactive_actions" ->
                listOf(
                    MindToolExercise(
                        id = "interrupted_pattern",
                        categoryId = "interactive_actions",
                        title = "Interrupted Pattern",
                        description = "Draw through imperfection. Notice your focus.",
                        durationMinutes = 2,
                    ),
                    MindToolExercise(
                        id = "disappearing_center",
                        categoryId = "interactive_actions",
                        title = "Disappearing Center",
                        description = "Draw around the center. Watch it fade.",
                        durationMinutes = 2,
                    ),
                )
            "quotes" ->
                listOf(
                    MindToolExercise(
                        id = "kind_line",
                        categoryId = "quotes",
                        title = "One kind line",
                        description = "A short quote that matches your mood.",
                        durationMinutes = 1,
                    ),
                    MindToolExercise(
                        id = "anchor_phrase",
                        categoryId = "quotes",
                        title = "Anchor phrase",
                        description = "Pick one sentence for today.",
                        durationMinutes = 1,
                    ),
                )
            else -> emptyList()
        }
    }
}
