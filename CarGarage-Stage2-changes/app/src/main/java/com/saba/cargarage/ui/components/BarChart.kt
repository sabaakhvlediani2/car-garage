package com.saba.cargarage.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saba.cargarage.stats.ChartEntry
import kotlin.math.max

/**
 * A vertical bar chart drawn entirely with Compose [Canvas] — no chart library.
 * Bars grow with a short entrance animation; value and category labels are
 * rendered through the underlying native canvas.
 */
@Composable
fun BarChart(
    entries: List<ChartEntry>,
    barColors: List<Color>,
    modifier: Modifier = Modifier,
    valueAsInt: Boolean = true
) {
    val progress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 700, easing = LinearOutSlowInEasing),
        label = "barGrow"
    )
    val maxValue = remember(entries) { max(entries.maxOfOrNull { it.value } ?: 0f, 1f) }

    val density = LocalDensity.current
    val labelPx = with(density) { 12.sp.toPx() }
    val valuePx = with(density) { 12.sp.toPx() }
    val labelColorArgb = androidx.compose.ui.graphics.Color(0xFF6B7690).toArgb()
    val valueColorArgb = androidx.compose.ui.graphics.Color(0xFF11203D).toArgb()

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        if (entries.isEmpty()) return@Canvas

        val bottomLabelSpace = labelPx * 2.2f
        val topValueSpace = valuePx * 1.6f
        val chartHeight = size.height - bottomLabelSpace - topValueSpace
        val slot = size.width / entries.size
        val barWidth = slot * 0.5f

        entries.forEachIndexed { index, entry ->
            val centerX = slot * index + slot / 2f
            val fullBarHeight = (entry.value / maxValue) * chartHeight
            val barHeight = fullBarHeight * progress
            val left = centerX - barWidth / 2f
            val top = topValueSpace + (chartHeight - barHeight)

            // Track (faint full-height background).
            drawRoundRect(
                color = barColors[index % barColors.size].copy(alpha = 0.12f),
                topLeft = Offset(left, topValueSpace),
                size = Size(barWidth, chartHeight),
                cornerRadius = CornerRadius(barWidth / 3f, barWidth / 3f)
            )
            // Actual bar.
            drawRoundRect(
                color = barColors[index % barColors.size],
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 3f, barWidth / 3f)
            )

            drawLabels(
                entry = entry,
                centerX = centerX,
                barTop = top,
                valuePx = valuePx,
                labelPx = labelPx,
                valueColorArgb = valueColorArgb,
                labelColorArgb = labelColorArgb,
                valueAsInt = valueAsInt
            )
        }
    }
}

private fun DrawScope.drawLabels(
    entry: ChartEntry,
    centerX: Float,
    barTop: Float,
    valuePx: Float,
    labelPx: Float,
    valueColorArgb: Int,
    labelColorArgb: Int,
    valueAsInt: Boolean
) {
    val valueText = if (valueAsInt) entry.value.toInt().toString()
    else "$" + String.format(java.util.Locale.US, "%,.0f", entry.value)

    drawContext.canvas.nativeCanvas.apply {
        val valuePaint = android.graphics.Paint().apply {
            color = valueColorArgb
            textSize = valuePx
            textAlign = android.graphics.Paint.Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }
        drawText(valueText, centerX, barTop - valuePx * 0.4f, valuePaint)

        val labelPaint = android.graphics.Paint().apply {
            color = labelColorArgb
            textSize = labelPx
            textAlign = android.graphics.Paint.Align.CENTER
            isAntiAlias = true
        }
        drawText(entry.label, centerX, size.height - labelPx * 0.5f, labelPaint)
    }
}
