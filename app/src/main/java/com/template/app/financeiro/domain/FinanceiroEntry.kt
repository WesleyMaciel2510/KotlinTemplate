package com.template.app.financeiro.domain

data class FinanceiroEntry(
    val description: String,
    val date: String,
    val amount: Double,
    val isIncome: Boolean
)
