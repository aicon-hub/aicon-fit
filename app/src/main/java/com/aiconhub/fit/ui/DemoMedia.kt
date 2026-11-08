package com.aiconhub.fit.ui

import android.net.Uri
import android.widget.VideoView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

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
        AnimImage(
            assetPath = anim,
            reduceMotion = reduceMotion,
            contentDescription = contentDescription,
            modifier = modifier
        )
        return
    }
    AndroidView(
        modifier = modifier,
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
    DisposableEffect(Unit) {
        onDispose { }
    }
}
