package com.aiconhub.fit.ui

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.viewinterop.AndroidView

/** Bundled demo playback with a real poster while loading or when motion is off. */
@Composable
fun DemoMedia(
    video: String?,
    anim: String,
    reduceMotion: Boolean,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    Box(modifier.background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
        AnimImage(
            assetPath = anim,
            reduceMotion = reduceMotion || video != null,
            contentDescription = contentDescription,
            modifier = Modifier.matchParentSize()
        )
        if (video != null && !reduceMotion) {
            key(video) {
                var failed by remember { mutableStateOf(false) }
                if (!failed) {
                    AndroidView(
                        modifier = Modifier.matchParentSize(),
                        factory = { ctx ->
                            VideoView(ctx).apply {
                                val resId = ctx.resources.getIdentifier(video, "raw", ctx.packageName)
                                setOnPreparedListener { player ->
                                    player.isLooping = true
                                    player.setVolume(0f, 0f)
                                    start()
                                }
                                setOnErrorListener { _, _, _ -> failed = true; true }
                                contentDescription?.let { this.contentDescription = it }
                                if (resId != 0) {
                                    setVideoURI(Uri.parse("android.resource://${ctx.packageName}/$resId"))
                                } else failed = true
                            }
                        },
                        update = { view -> view.contentDescription = contentDescription },
                        onRelease = { view -> view.stopPlayback() }
                    )
                }
            }
        }
    }
}
