package com.aiconhub.fit.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.sin
import kotlin.random.Random

/**
 * Press bounce for tappable elements: scales down a touch while pressed and
 * springs back on release. Small, but it makes every tap feel answered.
 */
fun Modifier.bounceClick(
    interactionSource: MutableInteractionSource,
    pressedScale: Float = 0.95f
): Modifier = composed {
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 500f),
        label = "pressBounce"
    )
    this.scale(scale)
}

/**
 * Slides and fades content in when it first appears. Use a small delayMillis
 * ladder to stagger lists of cards.
 */
@Composable
fun rememberEntranceProgress(delayMillis: Int = 0): Float {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        if (delayMillis > 0) kotlinx.coroutines.delay(delayMillis.toLong())
        progress.animateTo(1f, tween(420, easing = LinearEasing))
    }
    return progress.value
}

private data class ConfettiPiece(
    val x: Float,
    val speed: Float,
    val size: Float,
    val color: Color,
    val wobble: Float,
    val phase: Float
)

private val ConfettiColors = listOf(
    Color(0xFFFF6B2C), Color(0xFFFF9A5C), Color(0xFFFFD9C2),
    Color(0xFF7BD88A), Color(0xFFFFC24B), Color(0xFFFFF6EE)
)

/**
 * A rain of confetti that fills the available space. Particles loop forever
 * while this is on screen; pass reduceMotion = true to skip drawing entirely.
 */
@Composable
fun Confetti(modifier: Modifier = Modifier, reduceMotion: Boolean = false) {
    if (reduceMotion) return
    val pieces = remember {
        val rnd = Random(7)
        List(48) {
            ConfettiPiece(
                x = rnd.nextFloat(),
                speed = 0.35f + rnd.nextFloat() * 0.5f,
                size = 6f + rnd.nextFloat() * 10f,
                color = ConfettiColors[rnd.nextInt(ConfettiColors.size)],
                wobble = 10f + rnd.nextFloat() * 30f,
                phase = rnd.nextFloat() * 6.28f
            )
        }
    }
    val transition = rememberInfiniteTransition(label = "confetti")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Restart),
        label = "confettiTime"
    )
    Canvas(modifier = modifier) {
        pieces.forEach { p ->
            val cycle = (t * p.speed + p.phase / 6.28f) % 1f
            val y = cycle * (size.height + 80f) - 40f
            val x = p.x * size.width + sin(cycle * 6.28f * 2 + p.phase) * p.wobble
            drawCircle(color = p.color, radius = p.size / 2f, center = Offset(x, y))
        }
    }
}
