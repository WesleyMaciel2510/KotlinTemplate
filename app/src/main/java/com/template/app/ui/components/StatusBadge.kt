package com.template.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.template.app.domain.model.RecebivelStatus
import com.template.app.ui.theme.LocalAppColors

@Composable
fun StatusBadge(
    status: RecebivelStatus,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val (backgroundColor, contentColor, icon, label) = when (status) {
        RecebivelStatus.Pendente -> Quadruple(
            colors.pendingContainer,
            colors.onPendingContainer,
            Icons.Default.Schedule,
            "Pendente"
        )
        RecebivelStatus.Pago -> Quadruple(
            colors.paidContainer,
            colors.onPaidContainer,
            Icons.Default.CheckCircle,
            "Pago"
        )
        RecebivelStatus.Vencido -> Quadruple(
            colors.overdueContainer,
            colors.onOverdueContainer,
            Icons.Default.Error,
            "Vencido"
        )
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .semantics { contentDescription = "Status: $label" },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor
        )
    }
}

private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

@Preview
@Composable
private fun StatusBadgePreview() {
    com.template.app.ui.theme.TemplateAppTheme {
        Row {
            StatusBadge(status = RecebivelStatus.Pendente)
            Spacer(modifier = Modifier.width(8.dp))
            StatusBadge(status = RecebivelStatus.Pago)
            Spacer(modifier = Modifier.width(8.dp))
            StatusBadge(status = RecebivelStatus.Vencido)
        }
    }
}
