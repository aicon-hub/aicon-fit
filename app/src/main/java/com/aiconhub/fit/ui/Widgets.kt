package com.aiconhub.fit.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.aiconhub.fit.data.HistoryEntry
import com.aiconhub.fit.data.Quote
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

/** Circular progress ring showing completion, e.g. "2/6". Animates on change. */
@Composable
fun ProgressRing(done: Int, total: Int, sizeDp: Int, modifier: Modifier = Modifier) {
    val target = if (total > 0) done / total.toFloat() else 0f
    val progress by animateFloatAsState(targetValue = target, animationSpec = tween(700), label = "ring")
    val color = MaterialTheme.colorScheme.primary
    val track = MaterialTheme.colorScheme.surfaceVariant
    Box(modifier.width(sizeDp.dp).height(sizeDp.dp), contentAlignment = Alignment.Center) {
        androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) {
            val strokeWidth = size.minDimension * 0.14f
            val style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = strokeWidth,
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            drawArc(track, 0f, 360f, false, style = style)
            if (progress > 0f) drawArc(color, -90f, 360f * progress, false, style = style)
        }
        Text("$done/$total", style = MaterialTheme.typography.labelSmall)
    }
}

/** Minutes trained on each of the last 7 days as rounded bars; today glows. */
@Composable
fun WeekChart(history: List<HistoryEntry>, todayEpochDay: Long, modifier: Modifier = Modifier) {
    val days = (6 downTo 0).map { todayEpochDay - it }
    val minutes = days.map { d -> history.filter { it.epochDay == d }.sumOf { it.minutes } }
    val max = (minutes.maxOrNull() ?: 0).coerceAtLeast(1)
    val labels = days.map {
        Instant.ofEpochSecond(it * 86400).atZone(ZoneId.systemDefault())
            .dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault())
    }
    Row(
        modifier = modifier.fillMaxWidth().height(110.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        days.forEachIndexed { i, d ->
            val m = minutes[i]
            val isToday = d == todayEpochDay
            val barHeight = if (m > 0) (72f * m / max).coerceAtLeast(10f) else 4f
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (m > 0) {
                    Text("$m", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(2.dp))
                }
                Box(
                    Modifier
                        .width(20.dp)
                        .height(barHeight.dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
                        .background(
                            when {
                                m > 0 && isToday -> Orange
                                m > 0 -> OrangeDeep.copy(alpha = 0.55f)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    labels[i],
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/** A motivational quote with its attribution, styled with an ember accent bar. */
@Composable
fun QuoteCard(quote: Quote, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Box(
                Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(emberBrush())
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 16.dp)
            ) {
                Text(
                    "\u201C" + quote.text + "\u201D",
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    quote.author,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
