package com.template.app.financeiro.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.template.app.financeiro.domain.FinanceiroSummary
import java.util.Locale

@Composable
fun FinanceiroScreen(viewModel: FinanceiroViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    FinanceiroContent(state, viewModel::retry, viewModel::refresh)
}

@Composable
private fun FinanceiroContent(
    state: FinanceiroUiState,
    onRetry: () -> Unit,
    onRefresh: () -> Unit
) {
    when {
        state.isLoading -> FinanceiroLoading()
        state.errorMessage != null && state.data == null -> FinanceiroError(state.errorMessage, onRetry)
        state.data == null || state.data.entries.isEmpty() -> FinanceiroEmpty(onRetry)
        else -> FinanceiroSummaryContent(state.data, state.isRefreshing, state.refreshErrorMessage, onRefresh)
    }
}

@Composable
private fun FinanceiroSummaryContent(
    summary: FinanceiroSummary,
    isRefreshing: Boolean,
    refreshErrorMessage: String?,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Financeiro", style = MaterialTheme.typography.headlineSmall)
            if (isRefreshing) CircularProgressIndicator() else IconButton(onClick = onRefresh) {
                Icon(Icons.Filled.Refresh, contentDescription = "Atualizar finanças")
            }
        }
        if (refreshErrorMessage != null) Text(refreshErrorMessage, color = MaterialTheme.colorScheme.error)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Saldo", style = MaterialTheme.typography.labelLarge)
                Text(currency(summary.balance), style = MaterialTheme.typography.headlineMedium)
                Text("Entradas ${currency(summary.income)}", color = MaterialTheme.colorScheme.primary)
                Text("Saídas ${currency(summary.expenses)}", color = MaterialTheme.colorScheme.error)
            }
        }

        Text("Lançamentos", style = MaterialTheme.typography.titleLarge)
        summary.entries.forEach { entry ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(entry.description, style = MaterialTheme.typography.titleMedium)
                        Text(entry.date, style = MaterialTheme.typography.bodyMedium)
                    }
                    Text(
                        text = (if (entry.isIncome) "+" else "-") + currency(entry.amount),
                        color = if (entry.isIncome) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

private fun currency(value: Double): String = String.format(Locale("pt", "BR"), "R$ %.2f", value)

@Composable
private fun FinanceiroLoading() {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        CircularProgressIndicator()
        Spacer(Modifier.height(16.dp))
        Text("Carregando suas finanças...")
    }
}

@Composable
private fun FinanceiroError(message: String, onRetry: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(message, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) { Text("Tentar novamente") }
    }
}

@Composable
private fun FinanceiroEmpty(onRetry: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Ainda não há dados financeiros para mostrar.")
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = onRetry) { Text("Tentar novamente") }
    }
}

@Composable
fun FinanceiroScreenPreview() {
    com.template.app.ui.theme.TemplateAppTheme {
        FinanceiroContent(
            state = FinanceiroUiState(data = com.template.app.financeiro.data.FakeFinanceiroRepository.defaultSummary, isLoading = false),
            onRetry = {},
            onRefresh = {}
        )
    }
}
