package pl.nubet.studymood.domain.model

import org.jetbrains.compose.resources.DrawableResource
import studymood.composeapp.generated.resources.*

data class BrightSpotsStep(
    val illustration: DrawableResource,
    val title: String,
    val description: String,
    val buttonText: String,
)

object BrightSpotsSteps {
    val steps =
        listOf(
            BrightSpotsStep(
                illustration = Res.drawable.noun_nose_7440574,
                title = "Breathe through your nose",
                description = "Take a few slow breaths in and out through your nose.",
                buttonText = "Continue",
            ),
            BrightSpotsStep(
                illustration = Res.drawable.noun_body_scan_5261016,
                title = "Scan your body gently",
                description =
                    "Gently bring your attention from your forehead, down through your shoulders, chest and legs. Just notice where your body feels tense or relaxed.",
                buttonText = "Continue",
            ),
            BrightSpotsStep(
                illustration = Res.drawable.noun_house_plant_7159723,
                title = "Look around the room you're in right now.",
                description = "Allow yourself to notice what's right about the room.",
                buttonText = "Continue",
            ),
            BrightSpotsStep(
                illustration = Res.drawable.noun_flying_bird_6275970,
                title = "Look for simple beauty",
                description =
                    "If you can see outside, notice any small movement. Trees, clouds, birds, people. Notice the beauty of ordinary things.",
                buttonText = "Continue",
            ),
            BrightSpotsStep(
                illustration = Res.drawable.noun_friends_5823666,
                title = "Remember someone you appreciate",
                description =
                    "Think of someone in your life. Bring to mind one quality you genuinely appreciate about them.",
                buttonText = "Continue",
            ),
            BrightSpotsStep(
                illustration = Res.drawable.noun_self_compassion_8104175,
                title = "Turn that appreciation inward",
                description =
                    "Find a feeling inside of yourself that is good or neutral. Thank yourself for that feeling.",
                buttonText = "Finish",
            ),
        )

    val completionIllustration = Res.drawable.noun_spiral_6986117
    val completionTitle = "Reset complete"
    val completionDescription = "You gave your mind and body a short pause. Well done."

    const val TOTAL_STEPS = 6
}
