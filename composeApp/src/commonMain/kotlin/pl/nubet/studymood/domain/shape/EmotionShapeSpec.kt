package pl.nubet.studymood.domain.shape

import kotlin.math.abs
import pl.nubet.studymood.domain.model.Emotion

data class EmotionShapeSpec(
    val baseShape: CozyBaseShape,
    val rotationDeg: Float = 0f,
    val scale: Float = 1f,
    val wobble: Float = 0f,
    val hasSignatureMark: Boolean = false,
)

fun emotionShapeSpecFor(emotion: Emotion): EmotionShapeSpec {
    val labelHash = emotion.label.hashCode()
    val absHash = abs(labelHash)
    val pleasant = emotion.pleasantDefault
    val energy = emotion.energyDefault

    fun pick(mod: Int): Int = absHash % mod
    fun pickFloat(min: Float, max: Float, mod: Int = 100): Float {
        return min + (pick(mod) / mod.toFloat()) * (max - min)
    }

    val baseShape = selectCozyShape(pleasant, energy, pick(CozyBaseShape.entries.size))

    val rotationDeg =
        when (pick(3)) {
            0 -> -15f
            1 -> 0f
            else -> 15f
        }

    val scale = pickFloat(0.92f, 1.08f, 50)

    val wobble =
        when {
            energy > 70 -> pickFloat(0.15f, 0.25f, 30)
            energy < 30 -> pickFloat(0.0f, 0.10f, 30)
            else -> pickFloat(0.05f, 0.15f, 30)
        }

    val hasSignatureMark = pick(4) == 0

    return EmotionShapeSpec(
        baseShape = baseShape,
        rotationDeg = rotationDeg,
        scale = scale,
        wobble = wobble,
        hasSignatureMark = hasSignatureMark,
    )
}

private fun selectCozyShape(pleasant: Int, energy: Int, hashMod: Int): CozyBaseShape {
    return when {
        energy > 60 && pleasant > 60 ->
            when (hashMod % 7) {
                0 -> CozyBaseShape.Clover4
                1 -> CozyBaseShape.SoftStar
                2 -> CozyBaseShape.SoftPlus
                3 -> CozyBaseShape.CloudPuff
                4 -> CozyBaseShape.Broccoli
                5 -> CozyBaseShape.SoftArrowUp
                else -> CozyBaseShape.Clover3
            }

        energy > 60 && pleasant < 40 ->
            when (hashMod % 6) {
                0 -> CozyBaseShape.RoundedDiamond
                1 -> CozyBaseShape.SoftShield
                2 -> CozyBaseShape.SoftCross
                3 -> CozyBaseShape.SoftArrowRight
                4 -> CozyBaseShape.Bean
                else -> CozyBaseShape.PillSquare
            }

        energy < 40 && pleasant < 40 ->
            when (hashMod % 5) {
                0 -> CozyBaseShape.Bean
                1 -> CozyBaseShape.SoftOval
                2 -> CozyBaseShape.Mushroom
                3 -> CozyBaseShape.StackedPillsVertical
                else -> CozyBaseShape.CloudPuff
            }

        energy < 40 && pleasant > 60 ->
            when (hashMod % 6) {
                0 -> CozyBaseShape.SoftCircle
                1 -> CozyBaseShape.SoftHeart
                2 -> CozyBaseShape.CloudPuff
                3 -> CozyBaseShape.Clover3
                4 -> CozyBaseShape.SoftOval
                else -> CozyBaseShape.PillSquare
            }

        else ->
            when (hashMod % 8) {
                0 -> CozyBaseShape.PillSquare
                1 -> CozyBaseShape.RoundedDiamond
                2 -> CozyBaseShape.Clover4
                3 -> CozyBaseShape.SoftPlus
                4 -> CozyBaseShape.CloudPuff
                5 -> CozyBaseShape.StackedPillsHorizontal
                6 -> CozyBaseShape.SoftOval
                else -> CozyBaseShape.SoftCircle
            }
    }
}
