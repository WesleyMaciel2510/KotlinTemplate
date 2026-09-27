package com.template.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.template.app.domain.model.TimelineEvent
import com.template.app.domain.model.TimelineEventType

@Composable
fun InstallmentTimeline(
    events: List<TimelineEvent>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        events.forEachIndexed { index, event ->
            val isLast = index == events.size - 1
            TimelineItemRow(event = event, isLast = isLast)
        }
    }
}

@Composable
private fun TimelineItemRow(
    event: TimelineEvent,
    isLast: Boolean
) {
    val (icon, tint) = when (event.eventType) {
        TimelineEventType.Created -> Icons.Default.Add to MaterialTheme.colorScheme.primary
        TimelineEventType.Payment -> Icons.Default.Check to MaterialTheme.colorScheme.primary
        TimelineEventType.Adjustment -> Icons.Default.Edit to MaterialTheme.colorScheme.tertiary
        TimelineEventType.Cancellation -> Icons.Default.Cancel to MaterialTheme.colorScheme.error
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // Node Icon & Connecting Vertical Line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(tint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = event.title,
                    tint = tint,
                    modifier = Modifier.size(16.dp)
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 16.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = event.timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InstallmentTimelinePreview() {
    com.template.app.ui.theme.TemplateAppTheme {
        InstallmentTimeline(
            events = listOf(
                TimelineEvent("1", "Recebível Criado", "Fatura emitida com vencimento para 30/09/2026", "01/09/2026 10:00", TimelineEventType.Created),
                TimelineEvent("2", "Pagamento Parcial", "Recebido via PIX R$ 10.000,00", "15/09/2026 14:30", TimelineEventType.Payment)
            )
        )
    }
}
