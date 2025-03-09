package uk.ac.tees.mad.tuneflow.view.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.delay

// Animation extension functions
fun Modifier.pulsateEffect() = composed {
    val scale = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        while(true) {
            scale.animateTo(
                1.2f,
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )
            scale.animateTo(
                1f,
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )
            delay(1000)
        }
    }
    this.scale(scale.value)
}