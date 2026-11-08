package com.aiconhub.fit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

/** Circular progress ring showing plan completion, e.g. "2/6". */
@Composable
fun ProgressRing(done: Int, total: Int, sizeDp: Int, modifier: Modifier = Modifier) {
    val progress = if (total > 0) done / total.toFloat() else 0f
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

/** Minutes trained on each of the last 7 days as simple bars. */
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
        modifier = modifier.fillMaxWidth().height(96.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        days.forEachIndexed { i, _ ->
            val m = minutes[i]
            val barHeight = if (m > 0) (64f * m / max).coerceAtLeast(8f) else 3f
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (m > 0) {
                    Text("$m", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(2.dp))
                }
                Box(
                    Modifier
                        .width(18.dp)
                        .height(barHeight.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (m > 0) Orange else Surface2)
                )
                Spacer(Modifier.height(4.dp))
                Text(labels[i], style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/** A motivational quote with its attribution. */
@Composable
fun QuoteCard(quote: Quote, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(Modifier.fillMaxWidth()) {
            Text(
                "\"" + quote.text + "\"",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            Text(
                quote.author,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 16.dp)
            )
        }
    }
}
