package com.template.app.financeiro.data

import com.template.app.financeiro.domain.FinanceiroSummary

interface FinanceiroRepository {
    suspend fun getFinanceiroSummary(): FinanceiroSummary
}
