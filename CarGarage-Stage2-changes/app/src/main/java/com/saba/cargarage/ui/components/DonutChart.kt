package com.saba.cargarage.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saba.cargarage.stats.ChartEntry

/**
 * A donut (ring) chart drawn with Compose [Canvas]. Each entry gets a slice
 * proportional to its value; a legend is rendered beside it.
 */
@Composable
fun DonutChart(
    entries: List<ChartEntry>,
    colors: List<Color>,
    centerLabel: String,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label = "donutSweep"
    )
    val total = remember(entries) { entries.sumOf { it.value.toDouble() }.toFloat() }
    val onSurface = MaterialTheme.colorScheme.onSurface

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(150.dp)) {
            Canvas(modifier = Modifier.size(150.dp)) {
                if (total <= 0f) return@Canvas
                val stroke = size.minDimension * 0.16f
                val inset = stroke / 2f
                val arcSize = Size(size.width - stroke, size.height - stroke)
                var startAngle = -90f
                entries.forEachIndexed { index, entry ->
                    if (entry.value <= 0f) return@forEachIndexed
                    val sweep = (entry.value / total) * 360f * progress
                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = Offset(inset, inset),
                        size = arcSize,
                        style = Stroke(width = stroke, cap = StrokeCap.Butt)
                    )
                    startAngle += (entry.value / total) * 360f
                }
            }
            Text(
                centerLabel,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = onSurface
            )
        }

        Spacer(Modifier.width(20.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            entries.forEachIndexed { index, entry ->
                LegendItem(
                    color = colors[index % colors.size],
                    label = entry.label,
                    value = entry.count.toString()
                )
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(color = color, shape = CircleShape, modifier = Modifier.size(12.dp)) {}
        Spacer(Modifier.width(8.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.width(6.dp))
        Text(
            "($value)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
