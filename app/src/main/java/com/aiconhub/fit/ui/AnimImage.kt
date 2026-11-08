package com.aiconhub.fit.ui

import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Shows a bundled animated WebP exercise demo. Reloads the drawable when the
 * asset path changes (for example when the workout player moves to the next
 * exercise). With reduceMotion on, it stays on the first frame so nothing
 * moves on screen.
 */
@Composable
fun AnimImage(
    assetPath: String,
    reduceMotion: Boolean,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            ImageView(ctx).apply {
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
        },
        update = { view ->
            view.contentDescription = contentDescription
            if (view.tag != assetPath) {
                view.tag = assetPath
                try {
                    val bytes = view.context.assets.open(assetPath).use { it.readBytes() }
                    val source = ImageDecoder.createSource(java.nio.ByteBuffer.wrap(bytes))
                    view.setImageDrawable(ImageDecoder.decodeDrawable(source))
                } catch (e: Exception) {
                    // Missing asset: leave the view empty rather than crash.
                    view.setImageDrawable(null)
                }
            }
            val drawable = view.drawable
            if (drawable is AnimatedImageDrawable) {
                drawable.repeatCount = AnimatedImageDrawable.REPEAT_INFINITE
                if (reduceMotion) drawable.stop() else drawable.start()
            }
        }
    )
}
