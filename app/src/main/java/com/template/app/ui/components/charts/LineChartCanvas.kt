package com.template.app.ui.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.template.app.domain.model.MonthlyTrendPoint

@Composable
fun LineChartCanvas(
    title: String,
    data: List<MonthlyTrendPoint>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    secondaryLineColor: Color? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (data.isNotEmpty()) {
                val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                val textStyle = MaterialTheme.typography.labelSmall
                val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .semantics {
                            contentDescription = "Gráfico de tendência $title"
                        }
                ) {
                    val width = size.width
                    val height = size.height
                    val spacing = width / (data.size - 1)

                    val maxValue = (data.maxOfOrNull { maxOf(it.value, it.secondaryValue) } ?: 100f) * 1.15f
                    val minValue = 0f

                    // Draw background horizontal grid lines
                    for (i in 0..3) {
                        val y = height * (i / 3f)
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    // Main trend path
                    val path = Path()
                    val fillPath = Path()

                    data.forEachIndexed { index, point ->
                        val x = index * spacing
                        val y = height - ((point.value - minValue) / (maxValue - minValue)) * height

                        if (index == 0) {
                            path.moveTo(x, y)
                            fillPath.moveTo(x, height)
                            fillPath.lineTo(x, y)
                        } else {
                            val previousX = (index - 1) * spacing
                            val previousY = height - ((data[index - 1].value - minValue) / (maxValue - minValue)) * height
                            val controlX1 = previousX + (x - previousX) / 2f
                            val controlX2 = previousX + (x - previousX) / 2f

                            path.cubicTo(controlX1, previousY, controlX2, y, x, y)
                            fillPath.cubicTo(controlX1, previousY, controlX2, y, x, y)
                        }

                        if (index == data.size - 1) {
                            fillPath.lineTo(x, height)
                            fillPath.close()
                        }

                        // Draw point circle
                        drawCircle(
                            color = lineColor,
                            radius = 4.dp.toPx(),
                            center = Offset(x, y)
                        )
                    }

                    // Fill gradient under curve
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                lineColor.copy(alpha = 0.35f),
                                lineColor.copy(alpha = 0.0f)
                            )
                        )
                    )

                    // Draw main trend stroke
                    drawPath(
                        path = path,
                        color = lineColor,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Secondary line if supplied
                    if (secondaryLineColor != null && data.any { it.secondaryValue > 0f }) {
                        val secondaryPath = Path()
                        data.forEachIndexed { index, point ->
                            val x = index * spacing
                            val y = height - ((point.secondaryValue - minValue) / (maxValue - minValue)) * height

                            if (index == 0) {
                                secondaryPath.moveTo(x, y)
                            } else {
                                val previousX = (index - 1) * spacing
                                val previousY = height - ((data[index - 1].secondaryValue - minValue) / (maxValue - minValue)) * height
                                secondaryPath.cubicTo(previousX + (x - previousX)/2f, previousY, previousX + (x - previousX)/2f, y, x, y)
                            }
                        }
                        drawPath(
                            path = secondaryPath,
                            color = secondaryLineColor,
                            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LineChartCanvasPreview() {
    com.template.app.ui.theme.TemplateAppTheme {
        LineChartCanvas(
            title = "Recebimentos Mensais",
            data = listOf(
                MonthlyTrendPoint("Jan", 60f),
                MonthlyTrendPoint("Fev", 80f),
                MonthlyTrendPoint("Mar", 110f),
                MonthlyTrendPoint("Abr", 95f)
            )
        )
    }
}
