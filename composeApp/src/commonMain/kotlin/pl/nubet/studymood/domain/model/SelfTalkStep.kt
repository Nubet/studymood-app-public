package pl.nubet.studymood.domain.model

data class SelfTalkStep(
    val icon: String,
    val title: String,
    val description: String,
    val buttonText: String,
)

object SelfTalkSteps {
    val steps =
        listOf(
            SelfTalkStep(
                icon = "🤍",
                title = "Think of someone you love",
                description =
                    "Visualize them clearly in your mind. A family member, friend, or even a beloved pet.",
                buttonText = "Next",
            ),
            SelfTalkStep(
                icon = "😔",
                title = "See them feeling upset",
                description =
                    "Imagine them sitting across from you, looking tired, worried, or sad.",
                buttonText = "I see them",
            ),
            SelfTalkStep(
                icon = "🗣️",
                title = "Offer them kind words",
                description =
                    "What would you say to support them? Imagine yourself speaking softly to them now.",
                buttonText = "I've said it",
            ),
            SelfTalkStep(
                icon = "✨",
                title = "Now, say it to yourself",
                description =
                    "Imagine you are the one sitting there. Use the same words on yourself. Let them land.",
                buttonText = "Finish",
            ),
        )

    val completionIcon = "🧡"
    val completionTitle = "Well done"
    val completionDescription = "Carry this kindness with you through the rest of your day."

    const val TOTAL_STEPS = 4
}
