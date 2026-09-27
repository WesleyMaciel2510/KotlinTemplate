package com.template.app.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class KpiMetric(
    val id: String,
    val title: String,
    val value: String,
    val subtitle: String? = null,
    val isPositive: Boolean = true
)

@Immutable
data class MonthlyTrendPoint(
    val month: String,
    val value: Float,
    val secondaryValue: Float = 0f
)

@Immutable
data class TopClient(
    val id: String,
    val name: String,
    val initials: String,
    val totalBilled: String
)

@Immutable
data class ActivityEvent(
    val id: String,
    val title: String,
    val description: String,
    val timeAgo: String,
    val eventType: ActivityEventType
)

enum class ActivityEventType {
    PaymentReceived,
    InvoiceSent,
    OverdueAlert,
    ClientAdded
}

@Immutable
data class PaymentMethodShare(
    val method: String,
    val percentage: Float,
    val totalAmount: String
)

@Immutable
data class PaymentTransaction(
    val id: String,
    val clientName: String,
    val method: String,
    val amount: String,
    val date: String
)
