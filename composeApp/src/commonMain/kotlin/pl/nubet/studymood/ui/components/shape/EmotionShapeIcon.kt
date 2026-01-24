package pl.nubet.studymood.ui.components.shape

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.*
import pl.nubet.studymood.domain.model.Emotion
import pl.nubet.studymood.domain.shape.CozyBaseShape
import pl.nubet.studymood.domain.shape.EmotionShapeSpec
import pl.nubet.studymood.domain.shape.emotionShapeSpecFor
import pl.nubet.studymood.ui.theme.EmotionColors

private const val SHAPE_PADDING_PERCENT = 0.15f
private const val CORNER_RADIUS_FACTOR = 0.25f
private const val WOBBLE_AMPLITUDE = 0.08f

@Composable
fun EmotionShapeIcon(emotion: Emotion, size: Dp = 40.dp, modifier: Modifier = Modifier) {
    val spec = remember(emotion.id) { emotionShapeSpecFor(emotion) }
    val baseColor = remember(emotion.quadrant) { EmotionColors.forQuadrant(emotion.quadrant) }
    val variantColor =
        remember(emotion.id, baseColor) {
            EmotionColors.withVariation(baseColor, emotion.label.hashCode())
        }

    Canvas(modifier = modifier.size(size)) {
        val canvasSize = this.size
        val targetSize = canvasSize.minDimension * (1f - SHAPE_PADDING_PERCENT)
        val center = Offset(canvasSize.width / 2f, canvasSize.height / 2f)

        val path = createCozyShapePath(spec, Offset(0f, 0f), targetSize / 2f)

        val bounds = path.getBounds()

        if (bounds.width > 0f && bounds.height > 0f) {
            val maxDimension = max(bounds.width, bounds.height)
            val targetDimension = targetSize

            if (maxDimension > targetDimension) {
                val scale = targetDimension / maxDimension
                val boundsCenter =
                    Offset(bounds.left + bounds.width / 2f, bounds.top + bounds.height / 2f)
                val scaleMatrix = Matrix()
                scaleMatrix.translate(-boundsCenter.x, -boundsCenter.y)
                scaleMatrix.scale(scale, scale)
                scaleMatrix.translate(boundsCenter.x, boundsCenter.y)
                path.transform(scaleMatrix)
            }
        }

        val finalBounds = path.getBounds()

        val visualCenter =
            Offset(
                finalBounds.left + finalBounds.width / 2f,
                finalBounds.top + finalBounds.height / 2f,
            )

        val offsetX = center.x - visualCenter.x
        val offsetY = center.y - visualCenter.y

        val centerMatrix = Matrix()
        centerMatrix.translate(offsetX, offsetY)
        path.transform(centerMatrix)

        val brush = createCozyGradient(variantColor, canvasSize, center)

        drawPath(path = path, brush = brush, style = Fill)

        if (spec.hasSignatureMark) {
            drawSignatureMark(center, targetSize / 2f, variantColor)
        }
    }
}

private fun createCozyGradient(color: Color, canvasSize: Size, center: Offset): Brush {
    val lighter = EmotionColors.lightenColor(color, 0.12f)
    val darker = EmotionColors.darkenColor(color, 0.08f)

    return Brush.radialGradient(
        colors = listOf(lighter, color, darker),
        center = center,
        radius = canvasSize.minDimension * 0.5f,
    )
}

private fun createCozyShapePath(spec: EmotionShapeSpec, center: Offset, radius: Float): Path {
    val basePath =
        when (spec.baseShape) {
            CozyBaseShape.SoftCircle -> createSoftCircle(center, radius)
            CozyBaseShape.SoftOval -> createSoftOval(center, radius)
            CozyBaseShape.RoundedDiamond -> createRoundedDiamond(center, radius)
            CozyBaseShape.PillSquare -> createPillSquare(center, radius)
            CozyBaseShape.SoftShield -> createSoftShield(center, radius)
            CozyBaseShape.CloudPuff -> createCloudPuff(center, radius)
            CozyBaseShape.Mushroom -> createMushroom(center, radius)
            CozyBaseShape.Clover3 -> createClover(center, radius, 3)
            CozyBaseShape.Clover4 -> createClover(center, radius, 4)
            CozyBaseShape.Clover5 -> createClover(center, radius, 5)
            CozyBaseShape.SoftPlus -> createSoftPlus(center, radius)
            CozyBaseShape.SoftCross -> createSoftCross(center, radius)
            CozyBaseShape.SoftArrowUp -> createSoftArrow(center, radius, 0f)
            CozyBaseShape.SoftArrowRight -> createSoftArrow(center, radius, 90f)
            CozyBaseShape.StackedPillsVertical -> createStackedPills(center, radius, true)
            CozyBaseShape.StackedPillsHorizontal -> createStackedPills(center, radius, false)
            CozyBaseShape.Broccoli -> createBroccoli(center, radius)
            CozyBaseShape.SoftHeart -> createSoftHeart(center, radius)
            CozyBaseShape.SoftStar -> createSoftStar(center, radius)
            CozyBaseShape.Bean -> createBean(center, radius)
        }

    val matrix = Matrix()

    if (spec.rotationDeg != 0f) {
        matrix.translate(-center.x, -center.y)
        matrix.rotateZ(spec.rotationDeg)
        matrix.translate(center.x, center.y)
    }

    if (spec.scale != 1f) {
        matrix.translate(-center.x, -center.y)
        matrix.scale(spec.scale, spec.scale)
        matrix.translate(center.x, center.y)
    }

    basePath.transform(matrix)

    if (spec.wobble > 0.01f) {
        applyWobble(basePath, center, radius, spec.wobble)
    }

    return basePath
}

private fun createSoftCircle(center: Offset, radius: Float): Path {
    return Path().apply {
        addOval(Rect(center.x - radius, center.y - radius, center.x + radius, center.y + radius))
    }
}

private fun createSoftOval(center: Offset, radius: Float): Path {
    return Path().apply {
        val rx = radius * 1.3f
        val ry = radius * 0.8f
        addOval(Rect(center.x - rx, center.y - ry, center.x + rx, center.y + ry))
    }
}

private fun createRoundedDiamond(center: Offset, radius: Float): Path {
    val size = radius * 1.4f
    val cornerRadius = size * CORNER_RADIUS_FACTOR

    return Path().apply {
        moveTo(center.x, center.y - size)

        lineTo(center.x + size - cornerRadius, center.y - cornerRadius)
        quadraticTo(
            center.x + size,
            center.y,
            center.x + size - cornerRadius,
            center.y + cornerRadius,
        )

        lineTo(center.x + cornerRadius, center.y + size - cornerRadius)
        quadraticTo(
            center.x,
            center.y + size,
            center.x - cornerRadius,
            center.y + size - cornerRadius,
        )

        lineTo(center.x - size + cornerRadius, center.y + cornerRadius)
        quadraticTo(
            center.x - size,
            center.y,
            center.x - size + cornerRadius,
            center.y - cornerRadius,
        )

        lineTo(center.x - cornerRadius, center.y - size + cornerRadius)
        quadraticTo(
            center.x,
            center.y - size,
            center.x + cornerRadius,
            center.y - size + cornerRadius,
        )

        close()
    }
}

private fun createPillSquare(center: Offset, radius: Float): Path {
    val size = radius * 1.4f
    val cornerRadius = size * 0.4f

    return Path().apply {
        addRoundRect(
            RoundRect(
                Rect(center.x - size, center.y - size, center.x + size, center.y + size),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
            )
        )
    }
}

private fun createSoftShield(center: Offset, radius: Float): Path {
    val width = radius * 1.3f
    val height = radius * 1.5f

    return Path().apply {
        moveTo(center.x, center.y - height)

        cubicTo(
            center.x + width * 0.7f,
            center.y - height,
            center.x + width,
            center.y - height * 0.6f,
            center.x + width,
            center.y,
        )

        lineTo(center.x + width, center.y + height * 0.5f)

        quadraticTo(center.x + width * 0.5f, center.y + height, center.x, center.y + height * 0.9f)
        quadraticTo(
            center.x - width * 0.5f,
            center.y + height,
            center.x - width,
            center.y + height * 0.5f,
        )

        lineTo(center.x - width, center.y)

        cubicTo(
            center.x - width,
            center.y - height * 0.6f,
            center.x - width * 0.7f,
            center.y - height,
            center.x,
            center.y - height,
        )

        close()
    }
}

private fun createCloudPuff(center: Offset, radius: Float): Path {
    val puffRadius = radius * 0.5f
    val path = Path()

    path.addOval(
        Rect(center.x - puffRadius, center.y, center.x + puffRadius, center.y + puffRadius * 2)
    )

    path.addOval(
        Rect(
            center.x - puffRadius * 1.8f,
            center.y - puffRadius * 0.8f,
            center.x - puffRadius * 0.2f,
            center.y + puffRadius * 1.2f,
        )
    )

    path.addOval(
        Rect(
            center.x - puffRadius * 0.6f,
            center.y - puffRadius * 1.2f,
            center.x + puffRadius * 1.0f,
            center.y + puffRadius * 0.8f,
        )
    )

    path.addOval(
        Rect(
            center.x + puffRadius * 0.2f,
            center.y - puffRadius * 0.8f,
            center.x + puffRadius * 1.8f,
            center.y + puffRadius * 1.2f,
        )
    )

    return path
}

private fun createMushroom(center: Offset, radius: Float): Path {
    return Path().apply {
        val capRadius = radius * 1.2f
        addArc(
            Rect(
                center.x - capRadius,
                center.y - radius * 1.5f,
                center.x + capRadius,
                center.y + radius * 0.5f,
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 180f,
        )

        val stemWidth = radius * 0.5f
        val stemHeight = radius * 1.2f
        addRoundRect(
            RoundRect(
                Rect(
                    center.x - stemWidth,
                    center.y - radius * 0.2f,
                    center.x + stemWidth,
                    center.y + stemHeight,
                ),
                cornerRadius = CornerRadius(stemWidth, stemWidth),
            )
        )
    }
}

private fun createClover(center: Offset, radius: Float, lobes: Int): Path {
    val path = Path()
    val lobeRadius = radius * 0.6f
    val centerDist = radius * 0.4f
    val angleStep = (2 * PI) / lobes

    for (i in 0 until lobes) {
        val angle = angleStep * i - PI / 2
        val lobeCx = center.x + (centerDist * cos(angle)).toFloat()
        val lobeCy = center.y + (centerDist * sin(angle)).toFloat()

        path.addOval(
            Rect(lobeCx - lobeRadius, lobeCy - lobeRadius, lobeCx + lobeRadius, lobeCy + lobeRadius)
        )
    }

    val centerRadius = radius * 0.3f
    path.addOval(
        Rect(
            center.x - centerRadius,
            center.y - centerRadius,
            center.x + centerRadius,
            center.y + centerRadius,
        )
    )

    return path
}

private fun createSoftPlus(center: Offset, radius: Float): Path {
    val path = Path()
    val armLength = radius * 1.3f
    val armWidth = radius * 0.5f
    val cornerRadius = armWidth * 0.5f

    path.addRoundRect(
        RoundRect(
            Rect(
                center.x - armLength,
                center.y - armWidth,
                center.x + armLength,
                center.y + armWidth,
            ),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
        )
    )

    path.addRoundRect(
        RoundRect(
            Rect(
                center.x - armWidth,
                center.y - armLength,
                center.x + armWidth,
                center.y + armLength,
            ),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
        )
    )

    return path
}

private fun createSoftCross(center: Offset, radius: Float): Path {
    val path = Path()
    val armLength = radius * 1.2f
    val armWidth = radius * 0.65f
    val cornerRadius = armWidth * 0.4f

    path.addRoundRect(
        RoundRect(
            Rect(
                center.x - armLength,
                center.y - armWidth,
                center.x + armLength,
                center.y + armWidth,
            ),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
        )
    )

    path.addRoundRect(
        RoundRect(
            Rect(
                center.x - armWidth,
                center.y - armLength,
                center.x + armWidth,
                center.y + armLength,
            ),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
        )
    )

    return path
}

private fun createSoftArrow(center: Offset, radius: Float, rotationDeg: Float): Path {
    val path =
        Path().apply {
            val bodyWidth = radius * 0.5f
            val bodyHeight = radius * 0.8f
            val headWidth = radius * 1.2f
            val headHeight = radius * 0.8f
            val cornerRadius = bodyWidth * 0.5f

            addRoundRect(
                RoundRect(
                    Rect(
                        center.x - bodyWidth,
                        center.y + bodyHeight * 0.2f,
                        center.x + bodyWidth,
                        center.y + bodyHeight * 1.5f,
                    ),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                )
            )

            moveTo(center.x, center.y - headHeight)

            quadraticTo(
                center.x + headWidth * 0.8f,
                center.y - headHeight * 0.3f,
                center.x + headWidth * 0.5f,
                center.y + headHeight * 0.2f,
            )

            lineTo(center.x + bodyWidth, center.y + headHeight * 0.2f)
            lineTo(center.x - bodyWidth, center.y + headHeight * 0.2f)

            lineTo(center.x - headWidth * 0.5f, center.y + headHeight * 0.2f)
            quadraticTo(
                center.x - headWidth * 0.8f,
                center.y - headHeight * 0.3f,
                center.x,
                center.y - headHeight,
            )

            close()
        }

    if (rotationDeg != 0f) {
        val matrix = Matrix()
        matrix.translate(-center.x, -center.y)
        matrix.rotateZ(rotationDeg)
        matrix.translate(center.x, center.y)
        path.transform(matrix)
    }

    return path
}

private fun createStackedPills(center: Offset, radius: Float, vertical: Boolean): Path {
    val path = Path()
    val pillLength = radius * 0.8f
    val pillWidth = radius * 0.4f
    val spacing = radius * 0.3f
    val cornerRadius = pillWidth * 0.5f

    if (vertical) {
        for (i in -1..1) {
            path.addRoundRect(
                RoundRect(
                    Rect(
                        center.x - pillLength,
                        center.y + i * spacing - pillWidth,
                        center.x + pillLength,
                        center.y + i * spacing + pillWidth,
                    ),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                )
            )
        }
    } else {
        for (i in -1..1) {
            path.addRoundRect(
                RoundRect(
                    Rect(
                        center.x + i * spacing - pillWidth,
                        center.y - pillLength,
                        center.x + i * spacing + pillWidth,
                        center.y + pillLength,
                    ),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                )
            )
        }
    }

    return path
}

private fun createBroccoli(center: Offset, radius: Float): Path {
    val path = Path()
    val puffRadius = radius * 0.45f
    val stemWidth = radius * 0.4f
    val stemHeight = radius * 0.8f

    path.addRoundRect(
        RoundRect(
            Rect(center.x - stemWidth, center.y, center.x + stemWidth, center.y + stemHeight),
            cornerRadius = CornerRadius(stemWidth * 0.5f, stemWidth * 0.5f),
        )
    )

    path.addOval(Rect(center.x - puffRadius, center.y - radius, center.x + puffRadius, center.y))

    path.addOval(
        Rect(
            center.x - puffRadius * 1.6f,
            center.y - radius * 0.5f,
            center.x - puffRadius * 0.2f,
            center.y + radius * 0.5f,
        )
    )

    path.addOval(
        Rect(
            center.x + puffRadius * 0.2f,
            center.y - radius * 0.5f,
            center.x + puffRadius * 1.6f,
            center.y + radius * 0.5f,
        )
    )

    return path
}

private fun createSoftHeart(center: Offset, radius: Float): Path {
    return Path().apply {
        val width = radius * 1.2f
        val height = radius * 1.3f

        moveTo(center.x, center.y + height * 0.7f)

        cubicTo(
            center.x - width * 0.5f,
            center.y + height * 0.3f,
            center.x - width,
            center.y,
            center.x - width * 0.6f,
            center.y - height * 0.3f,
        )

        cubicTo(
            center.x - width * 0.3f,
            center.y - height * 0.7f,
            center.x - width * 0.1f,
            center.y - height * 0.8f,
            center.x,
            center.y - height * 0.4f,
        )

        cubicTo(
            center.x + width * 0.1f,
            center.y - height * 0.8f,
            center.x + width * 0.3f,
            center.y - height * 0.7f,
            center.x + width * 0.6f,
            center.y - height * 0.3f,
        )

        cubicTo(
            center.x + width,
            center.y,
            center.x + width * 0.5f,
            center.y + height * 0.3f,
            center.x,
            center.y + height * 0.7f,
        )

        close()
    }
}

private fun createSoftStar(center: Offset, radius: Float): Path {
    val lobes = 5
    val outerRadius = radius * 1.2f
    val innerRadius = radius * 0.7f
    val angleStep = PI / lobes

    return Path().apply {
        val points = mutableListOf<Offset>()

        for (i in 0 until lobes * 2) {
            val angle = angleStep * i - PI / 2
            val r = if (i % 2 == 0) outerRadius else innerRadius
            points.add(
                Offset(center.x + (r * cos(angle)).toFloat(), center.y + (r * sin(angle)).toFloat())
            )
        }

        moveTo(points[0].x, points[0].y)

        for (i in 1 until points.size) {
            val curr = points[i]
            val next = points[(i + 1) % points.size]

            val ctrlX = curr.x
            val ctrlY = curr.y
            val endX = (curr.x + next.x) / 2f
            val endY = (curr.y + next.y) / 2f

            quadraticTo(ctrlX, ctrlY, endX, endY)
        }

        close()
    }
}

private fun createBean(center: Offset, radius: Float): Path {
    return Path().apply {
        val width = radius * 1.3f
        val height = radius * 1.5f

        moveTo(center.x, center.y - height)

        cubicTo(
            center.x + width * 0.8f,
            center.y - height * 0.7f,
            center.x + width,
            center.y,
            center.x + width * 0.6f,
            center.y + height * 0.8f,
        )

        quadraticTo(center.x, center.y + height, center.x - width * 0.6f, center.y + height * 0.8f)

        cubicTo(
            center.x - width * 0.9f,
            center.y + height * 0.2f,
            center.x - width * 0.7f,
            center.y - height * 0.3f,
            center.x,
            center.y - height,
        )

        close()
    }
}

private fun applyWobble(path: Path, center: Offset, radius: Float, wobbleAmount: Float) {
    val wobbleStrength = radius * wobbleAmount * WOBBLE_AMPLITUDE

    val matrix = Matrix()
    matrix.translate(-center.x, -center.y)
    matrix.scale(1f + wobbleStrength * 0.5f, 1f - wobbleStrength * 0.3f)
    matrix.translate(center.x, center.y)
    path.transform(matrix)
}

private fun DrawScope.drawSignatureMark(center: Offset, radius: Float, color: Color) {
    val markRadius = radius * 0.12f
    val markOffset = Offset(center.x + radius * 0.55f, center.y - radius * 0.55f)

    drawCircle(
        color = EmotionColors.darkenColor(color, 0.25f),
        radius = markRadius,
        center = markOffset,
        alpha = 0.7f,
    )
}
