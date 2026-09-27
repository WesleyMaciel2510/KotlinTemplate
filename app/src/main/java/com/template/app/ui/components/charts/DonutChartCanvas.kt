package com.template.app.ui.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.template.app.domain.model.PaymentMethodShare
import com.template.app.ui.theme.LocalAppColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DonutChartCanvas(
    title: String,
    shares: List<PaymentMethodShare>,
    modifier: Modifier = Modifier
) {
    val palette = LocalAppColors.current.chartPalette

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (shares.isNotEmpty()) {
                val total = shares.sumOf { it.percentage.toDouble() }.toFloat()
                Canvas(
                    modifier = Modifier
                        .size(160.dp)
                        .semantics { contentDescription = "Gráfico de rosca $title" }
                ) {
                    val strokeWidth = 28.dp.toPx()
                    var startAngle = -90f
                    shares.forEachIndexed { index, share ->
                        val sweepAngle = (share.percentage / total) * 360f
                        drawArc(
                            color = palette[index % palette.size],
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            style = Stroke(width = strokeWidth)
                        )
                        startAngle += sweepAngle
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    shares.forEachIndexed { index, share ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(palette[index % palette.size])
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${share.method} (${share.percentage.toInt()}%)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DonutChartCanvasPreview() {
    com.template.app.ui.theme.TemplateAppTheme {
        DonutChartCanvas(
            title = "Meios de Pagamento",
            shares = listOf(
                PaymentMethodShare("Pix", 50f, "R$ 5.000"),
                PaymentMethodShare("Cartão", 30f, "R$ 3.000"),
                PaymentMethodShare("Boleto", 20f, "R$ 2.000")
            )
        )
    }
}
