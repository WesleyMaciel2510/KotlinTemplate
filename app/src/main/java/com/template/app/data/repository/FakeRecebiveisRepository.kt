package com.template.app.data.repository

import com.template.app.domain.model.RecebivelItem
import com.template.app.domain.model.RecebivelStatus
import com.template.app.domain.model.TimelineEvent
import com.template.app.domain.model.TimelineEventType
import com.template.app.domain.repository.RecebiveisRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeRecebiveisRepository @Inject constructor() : RecebiveisRepository {

    private val mockItems = listOf(
        RecebivelItem(
            id = "REC-101",
            clientName = "Acme Corporation",
            clientInitials = "AC",
            description = "Licenciamento de Software ERP - Parcela 2/3",
            originalAmount = "R$ 15.000,00",
            remainingAmount = "R$ 5.000,00",
            originalAmountValue = 15000f,
            remainingAmountValue = 5000f,
            dueDate = "30 Set 2026",
            status = RecebivelStatus.Pendente,
            timeline = listOf(
                TimelineEvent("t1", "Recebível Criado", "Fatura emitida com vencimento para 30/09/2026", "01/09/2026 10:00", TimelineEventType.Created),
                TimelineEvent("t2", "Pagamento Parcial", "Recebido via PIX R$ 10.000,00", "15/09/2026 14:30", TimelineEventType.Payment)
            )
        ),
        RecebivelItem(
            id = "REC-102",
            clientName = "TechSolutions Ltda",
            clientInitials = "TS",
            description = "Consultoria em Nuvem AWS & DevOps",
            originalAmount = "R$ 8.400,00",
            remainingAmount = "R$ 8.400,00",
            originalAmountValue = 8400f,
            remainingAmountValue = 8400f,
            dueDate = "15 Set 2026",
            status = RecebivelStatus.Vencido,
            timeline = listOf(
                TimelineEvent("t3", "Recebível Criado", "Fatura emitida com vencimento para 15/09/2026", "15/08/2026 09:15", TimelineEventType.Created),
                TimelineEvent("t4", "Notificação de Cobrança", "E-mail automático enviado ao cliente", "16/09/2026 08:00", TimelineEventType.Adjustment)
            )
        ),
        RecebivelItem(
            id = "REC-103",
            clientName = "Global Logistics S.A.",
            clientInitials = "GL",
            description = "Desenvolvimento de App Mobile",
            originalAmount = "R$ 24.000,00",
            remainingAmount = "R$ 0,00",
            originalAmountValue = 24000f,
            remainingAmountValue = 0f,
            dueDate = "20 Set 2026",
            status = RecebivelStatus.Pago,
            timeline = listOf(
                TimelineEvent("t5", "Recebível Criado", "Fatura emitida", "01/08/2026 11:00", TimelineEventType.Created),
                TimelineEvent("t6", "Pagamento Integral", "Recebido via Transferência R$ 24.000,00", "20/09/2026 16:45", TimelineEventType.Payment)
            )
        ),
        RecebivelItem(
            id = "REC-104",
            clientName = "Nexus Inovações",
            clientInitials = "NI",
            description = "Suporte Técnico Mensal",
            originalAmount = "R$ 3.900,00",
            remainingAmount = "R$ 3.900,00",
            originalAmountValue = 3900f,
            remainingAmountValue = 3900f,
            dueDate = "05 Out 2026",
            status = RecebivelStatus.Pendente,
            timeline = listOf(
                TimelineEvent("t7", "Recebível Criado", "Fatura recorrente gerada", "05/09/2026 00:01", TimelineEventType.Created)
            )
        ),
        RecebivelItem(
            id = "REC-105",
            clientName = "Vanguard Comércio",
            clientInitials = "VC",
            description = "Treinamento de Equipe Comercial",
            originalAmount = "R$ 6.200,00",
            remainingAmount = "R$ 6.200,00",
            originalAmountValue = 6200f,
            remainingAmountValue = 6200f,
            dueDate = "10 Set 2026",
            status = RecebivelStatus.Vencido,
            timeline = listOf(
                TimelineEvent("t8", "Recebível Criado", "Fatura emitia", "10/08/2026 15:30", TimelineEventType.Created)
            )
        )
    )

    override fun getRecebiveis(): Flow<List<RecebivelItem>> = flow {
        delay(400)
        emit(mockItems)
    }

    override fun getRecebivelById(id: String): Flow<RecebivelItem?> = flow {
        delay(300)
        emit(mockItems.find { it.id == id } ?: mockItems.firstOrNull())
    }
}
