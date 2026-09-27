package com.template.app.domain.repository

import com.template.app.domain.model.ActivityEvent
import com.template.app.domain.model.KpiMetric
import com.template.app.domain.model.MonthlyTrendPoint
import com.template.app.domain.model.PaymentMethodShare
import com.template.app.domain.model.PaymentTransaction
import com.template.app.domain.model.TopClient
import kotlinx.coroutines.flow.Flow

interface FinanceRepository {
    fun getKpiMetrics(): Flow<List<KpiMetric>>
    fun getMonthlyReceiptsTrend(): Flow<List<MonthlyTrendPoint>>
    fun getTopClients(): Flow<List<TopClient>>
    fun getRecentActivities(): Flow<List<ActivityEvent>>
    fun getPaymentMethodShares(): Flow<List<PaymentMethodShare>>
    fun getRecentTransactions(): Flow<List<PaymentTransaction>>
    fun getDelinquencyTrend(): Flow<List<MonthlyTrendPoint>>
}
