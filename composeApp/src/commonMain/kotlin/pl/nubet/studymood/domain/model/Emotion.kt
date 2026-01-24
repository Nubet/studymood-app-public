package pl.nubet.studymood.domain.model

data class Emotion(
    val id: String,
    val label: String,
    val synonym: String? = null,
    val description: String? = null,
    val pleasantDefault: Int,
    val energyDefault: Int,
    val quadrant: Int,
    val custom: Boolean = false,
)
