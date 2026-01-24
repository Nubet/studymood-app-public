package pl.nubet.studymood.domain.model

enum class Quadrant {
    Yellow,
    Green,
    Blue,
    Red,
}

fun getQuadrantFromCoordinates(pleasant: Int, energy: Int): Quadrant {
    return when {
        pleasant >= 50 && energy >= 50 -> Quadrant.Yellow
        pleasant >= 50 && energy < 50 -> Quadrant.Green
        pleasant < 50 && energy < 50 -> Quadrant.Blue
        else -> Quadrant.Red
    }
}

fun Quadrant.toInt(): Int {
    return when (this) {
        Quadrant.Yellow -> 1
        Quadrant.Red -> 2
        Quadrant.Blue -> 3
        Quadrant.Green -> 4
    }
}
