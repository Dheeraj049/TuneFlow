package uk.ac.tees.mad.tuneflow.view.utils

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

@Composable
fun WaveLine(index: Int, color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 500f, animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(index * 500)
        ), label = "wave offset"
    )

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val path = Path()
        val width = size.width
        val height = size.height

        path.moveTo(0f, height * 0.5f + offsetY)

        for (x in 0..width.toInt() step 50) {
            path.quadraticTo(
                x + 25f, height * 0.5f + offsetY - 25, x + 50f, height * 0.5f + offsetY
            )
        }

        path.lineTo(width, height)
        path.lineTo(0f, height)
        path.close()

        drawPath(path, color)
    }
}