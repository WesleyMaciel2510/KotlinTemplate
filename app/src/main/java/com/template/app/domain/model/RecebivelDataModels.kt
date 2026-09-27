package com.template.app.domain.model

import androidx.compose.runtime.Immutable

@Immutable
enum class RecebivelStatus {
    Pendente,
    Pago,
    Vencido
}

@Immutable
enum class RecebivelFilterOption {
    Todos,
    Pendentes,
    Pagos,
    Vencidos
}

@Immutable
data class TimelineEvent(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: String,
    val eventType: TimelineEventType
)

enum class TimelineEventType {
    Created,
    Payment,
    Adjustment,
    Cancellation
}

@Immutable
data class RecebivelItem(
    val id: String,
    val clientName: String,
    val clientInitials: String,
    val description: String,
    val originalAmount: String,
    val remainingAmount: String,
    val originalAmountValue: Float,
    val remainingAmountValue: Float,
    val dueDate: String,
    val status: RecebivelStatus,
    val timeline: List<TimelineEvent> = emptyList()
)
