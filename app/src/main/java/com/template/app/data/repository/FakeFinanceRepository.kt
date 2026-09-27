package com.template.app.data.repository

import com.template.app.domain.model.ActivityEvent
import com.template.app.domain.model.ActivityEventType
import com.template.app.domain.model.KpiMetric
import com.template.app.domain.model.MonthlyTrendPoint
import com.template.app.domain.model.PaymentMethodShare
import com.template.app.domain.model.PaymentTransaction
import com.template.app.domain.model.TopClient
import com.template.app.domain.repository.FinanceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeFinanceRepository @Inject constructor() : FinanceRepository {

    override fun getKpiMetrics(): Flow<List<KpiMetric>> = flow {
        delay(400)
        emit(
            listOf(
                KpiMetric("1", "Total Recebíveis", "R$ 148.500,00", "+12.4% este mês", isPositive = true),
                KpiMetric("2", "Valor Pendente", "R$ 32.100,00", "8 faturas em aberto", isPositive = false),
                KpiMetric("3", "Valor Recebido", "R$ 116.400,00", "Meta atingida em 92%", isPositive = true),
                KpiMetric("4", "Valor Vencido", "R$ 4.350,00", "-2.1% vs mês anterior", isPositive = true),
                KpiMetric("5", "Taxa de Recebimento", "96.4%", "Top 5% do setor", isPositive = true)
            )
        )
    }

    override fun getMonthlyReceiptsTrend(): Flow<List<MonthlyTrendPoint>> = flow {
        delay(400)
        emit(
            listOf(
                MonthlyTrendPoint("Jan", 65f, 50f),
                MonthlyTrendPoint("Fev", 78f, 60f),
                MonthlyTrendPoint("Mar", 92f, 75f),
                MonthlyTrendPoint("Abr", 85f, 70f),
                MonthlyTrendPoint("Mai", 110f, 95f),
                MonthlyTrendPoint("Jun", 116f, 105f)
            )
        )
    }

    override fun getTopClients(): Flow<List<TopClient>> = flow {
        delay(400)
        emit(
            listOf(
                TopClient("1", "Acme Corporation", "AC", "R$ 45.200,00"),
                TopClient("2", "TechSolutions Ltda", "TS", "R$ 32.800,00"),
                TopClient("3", "Global Logistics", "GL", "R$ 24.150,00"),
                TopClient("4", "Nexus Inovações", "NI", "R$ 18.900,00"),
                TopClient("5", "Vanguard Comércio", "VC", "R$ 12.400,00")
            )
        )
    }

    override fun getRecentActivities(): Flow<List<ActivityEvent>> = flow {
        delay(400)
        emit(
            listOf(
                ActivityEvent("1", "Pagamento Recebido", "Acme Corp pagou fatura #1042", "Há 15 min", ActivityEventType.PaymentReceived),
                ActivityEvent("2", "Fatura Enviada", "Fatura #1045 enviada para TechSolutions", "Há 2 horas", ActivityEventType.InvoiceSent),
                ActivityEvent("3", "Alerta de Vencimento", "Fatura #1038 vence em 2 dias", "Há 4 horas", ActivityEventType.OverdueAlert),
                ActivityEvent("4", "Novo Cliente", "Vanguard Comércio cadastrado", "Ontem", ActivityEventType.ClientAdded)
            )
        )
    }

    override fun getPaymentMethodShares(): Flow<List<PaymentMethodShare>> = flow {
        delay(400)
        emit(
            listOf(
                PaymentMethodShare("Pix", 52f, "R$ 60.528,00"),
                PaymentMethodShare("Cartão de Crédito", 28f, "R$ 32.592,00"),
                PaymentMethodShare("Boleto", 15f, "R$ 17.460,00"),
                PaymentMethodShare("Transferência", 5f, "R$ 5.820,00")
            )
        )
    }

    override fun getRecentTransactions(): Flow<List<PaymentTransaction>> = flow {
        delay(400)
        emit(
            listOf(
                PaymentTransaction("101", "Acme Corporation", "Pix", "R$ 12.500,00", "Hoje, 14:32"),
                PaymentTransaction("102", "TechSolutions Ltda", "Cartão de Crédito", "R$ 8.400,00", "Hoje, 11:15"),
                PaymentTransaction("103", "Global Logistics", "Boleto", "R$ 5.350,00", "Ontem, 16:45"),
                PaymentTransaction("104", "Nexus Inovações", "Pix", "R$ 3.900,00", "Ontem, 09:20")
            )
        )
    }

    override fun getDelinquencyTrend(): Flow<List<MonthlyTrendPoint>> = flow {
        delay(400)
        emit(
            listOf(
                MonthlyTrendPoint("Jan", 8.5f),
                MonthlyTrendPoint("Fev", 6.2f),
                MonthlyTrendPoint("Mar", 5.8f),
                MonthlyTrendPoint("Abr", 4.1f),
                MonthlyTrendPoint("Mai", 3.9f),
                MonthlyTrendPoint("Jun", 3.6f)
            )
        )
    }
}
