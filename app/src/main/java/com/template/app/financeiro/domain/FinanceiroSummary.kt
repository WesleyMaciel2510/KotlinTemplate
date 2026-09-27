package com.template.app.financeiro.domain

data class FinanceiroSummary(
    val balance: Double,
    val income: Double,
    val expenses: Double,
    val entries: List<FinanceiroEntry>
)
