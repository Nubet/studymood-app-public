package pl.nubet.studymood.domain.model

data class NormalizedPoint(val x: Float, val y: Float) {
    companion object {
        val ZERO = NormalizedPoint(0f, 0f)
    }
}

data class DrawingStroke(val points: List<NormalizedPoint>) {
    val isEmpty: Boolean
        get() = points.isEmpty()

    val size: Int
        get() = points.size
}

data class PatternTemplate(
    val id: String,
    val label: String,
    val microcopy: String,
    val generator: (width: Float, height: Float) -> List<NormalizedPoint>,
)
