package uk.ac.tees.mad.tuneflow.view.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoadingScreen() {

    // Title scale animation
    val scale = remember { Animatable(0.3f) }

    LaunchedEffect(key1 = true) {
        // Animate title scale
        scale.animateTo(
            targetValue = 1f, animationSpec = tween(
                durationMillis = 1000, easing = EaseOutBack
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.scale(scale.value)
        ) {
            // App Name
            Text(
                text = "TuneFlow", style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold, fontSize = 48.sp
                ), color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Animated sound wave
            WaveAnimation(numWaves = 24)

            Spacer(modifier = Modifier.height(16.dp))

            // Tagline
            Text(
                text = "Feel the music, flow with the rhythm!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                fontStyle = FontStyle.Italic
            )
        }
    }
}