package com.aiconhub.fit.ui

import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Shows a bundled animated WebP exercise demo. With reduceMotion on, it stays
 * on the first frame so nothing moves on screen.
 */
@Composable
fun AnimImage(
    assetPath: String,
    reduceMotion: Boolean,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            ImageView(ctx).apply {
                scaleType = ImageView.ScaleType.FIT_CENTER
                contentDescription?.let { this.contentDescription = it }
                try {
                    val bytes = ctx.assets.open(assetPath).use { it.readBytes() }
                    val source = ImageDecoder.createSource(java.nio.ByteBuffer.wrap(bytes))
                    val drawable = ImageDecoder.decodeDrawable(source)
                    setImageDrawable(drawable)
                    if (drawable is AnimatedImageDrawable && !reduceMotion) {
                        drawable.repeatCount = AnimatedImageDrawable.REPEAT_INFINITE
                        drawable.start()
                    }
                } catch (e: Exception) {
                    // Missing asset: leave the view empty rather than crash.
                }
            }
        },
        update = { view ->
            val drawable = view.drawable
            if (drawable is AnimatedImageDrawable) {
                if (reduceMotion) drawable.stop() else drawable.start()
            }
        }
    )
}
