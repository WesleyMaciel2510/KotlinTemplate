package com.template.app.financeiro.data

import com.template.app.financeiro.domain.FinanceiroEntry
import com.template.app.financeiro.domain.FinanceiroSummary

class FakeFinanceiroRepository(
    private var nextResult: Result<FinanceiroSummary> = Result.success(defaultSummary)
) : FinanceiroRepository {

    override suspend fun getFinanceiroSummary(): FinanceiroSummary = nextResult.getOrThrow()

    fun setNextResult(result: Result<FinanceiroSummary>) {
        nextResult = result
    }

    companion object {
        val defaultSummary = FinanceiroSummary(
            balance = 2450.75,
            income = 5200.00,
            expenses = 2749.25,
            entries = listOf(
                FinanceiroEntry("Salário", "Hoje", 5200.00, isIncome = true),
                FinanceiroEntry("Mercado", "Ontem", 240.50, isIncome = false),
                FinanceiroEntry("Transporte", "12/09/2026", 85.00, isIncome = false)
            )
        )
    }
}
