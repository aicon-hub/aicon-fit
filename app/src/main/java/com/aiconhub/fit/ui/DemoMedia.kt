package com.aiconhub.fit.ui

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Soft ember backdrop that sits behind every demo, so the area never reads
 * as a black hole while media loads (or in previews where native views do
 * not draw).
 */
@Composable
private fun DemoBackdrop(modifier: Modifier, label: String?, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier.background(
            Brush.radialGradient(
                listOf(
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.colorScheme.surface
                )
            )
        ),
        contentAlignment = Alignment.Center
    ) {
        if (label != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        content()
    }
}

/**
 * Shows an exercise demo. When the exercise has a real licensed demo video
 * (a raw resource name in its `video` field) this plays it muted and
 * looping; otherwise it falls back to the bundled animated WebP. With
 * reduceMotion on, the video stays paused on its first frame.
 */
@Composable
fun DemoMedia(
    video: String?,
    anim: String,
    reduceMotion: Boolean,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    if (video == null) {
        DemoBackdrop(modifier, "Demo") {
            AnimImage(
                assetPath = anim,
                reduceMotion = reduceMotion,
                contentDescription = contentDescription,
                modifier = Modifier.matchParentSize()
            )
        }
        return
    }
    DemoBackdrop(modifier, "Demo") {
        AndroidView(
            modifier = Modifier.matchParentSize(),
            factory = { ctx ->
                VideoView(ctx).apply {
                    val resId = ctx.resources.getIdentifier(video, "raw", ctx.packageName)
                    if (resId != 0) {
                        setVideoURI(Uri.parse("android.resource://${ctx.packageName}/$resId"))
                        setOnPreparedListener { mp ->
                            mp.isLooping = true
                            mp.setVolume(0f, 0f)
                            if (reduceMotion) {
                                seekTo(1)
                                pause()
                            } else {
                                start()
                            }
                        }
                    }
                }
            },
            update = { view ->
                view.contentDescription = contentDescription
                if (reduceMotion) view.pause() else view.start()
            }
        )
    }
}
