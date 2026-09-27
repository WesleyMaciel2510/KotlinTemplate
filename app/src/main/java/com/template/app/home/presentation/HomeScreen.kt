package com.template.app.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.template.app.home.domain.HomeOverview
import com.template.app.ui.screens.FinanceContentScreen

@Composable
fun HomeScreen(
    onNavigateToQrScanner: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        state = state,
        onRetry = viewModel::retry,
        onRefresh = viewModel::refresh,
        onNavigateToQrScanner = onNavigateToQrScanner,
        includeFinanceContent = true
    )
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onNavigateToQrScanner: () -> Unit,
    includeFinanceContent: Boolean = true
) {
    when {
        state.isLoading -> LoadingState("Carregando seu resumo...")
        state.errorMessage != null && state.data == null -> ErrorState(state.errorMessage, onRetry)
        state.data == null || (state.data.metrics.isEmpty() && state.data.recentItems.isEmpty()) -> EmptyState(
            message = "Ainda não há informações para mostrar no seu painel.",
            actionLabel = "Tentar novamente",
            onAction = onRetry
        )
        else -> HomeOverviewContent(
            overview = state.data,
            isRefreshing = state.isRefreshing,
            refreshErrorMessage = state.refreshErrorMessage,
            onRefresh = onRefresh,
            onNavigateToQrScanner = onNavigateToQrScanner,
            includeFinanceContent = includeFinanceContent
        )
    }
}

@Composable
private fun HomeOverviewContent(
    overview: HomeOverview,
    isRefreshing: Boolean,
    refreshErrorMessage: String?,
    onRefresh: () -> Unit,
    onNavigateToQrScanner: () -> Unit,
    includeFinanceContent: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(overview.greeting, style = MaterialTheme.typography.headlineSmall)
                Text(overview.summary, style = MaterialTheme.typography.bodyMedium)
            }
            if (isRefreshing) CircularProgressIndicator() else IconButton(onClick = onRefresh) {
                Icon(Icons.Filled.Refresh, contentDescription = "Atualizar painel")
            }
        }

        if (refreshErrorMessage != null) Text(refreshErrorMessage, color = MaterialTheme.colorScheme.error)
        Text("Visão geral", style = MaterialTheme.typography.titleLarge)
        overview.metrics.chunked(2).forEach { rowMetrics ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowMetrics.forEach { metric ->
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(metric.label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(metric.value, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
                if (rowMetrics.size == 1) Spacer(Modifier.weight(1f))
            }
        }

        Text("Recentes", style = MaterialTheme.typography.titleLarge)
        overview.recentItems.forEach { item ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(item.title, style = MaterialTheme.typography.titleMedium)
                    Text(item.detail, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        Button(onClick = onNavigateToQrScanner, modifier = Modifier.fillMaxWidth()) {
            Text("Abrir Scanner de QR Code")
        }
        if (includeFinanceContent) {
            Text("Análises financeiras", style = MaterialTheme.typography.titleLarge)
            FinanceContentScreen(modifier = Modifier.fillMaxWidth().height(720.dp))
        }
    }
}

@Composable
private fun LoadingState(message: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(16.dp))
        Text(message)
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(message, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) { Text("Tentar novamente") }
    }
}

@Composable
private fun EmptyState(message: String, actionLabel: String, onAction: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(message, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = onAction) { Text(actionLabel) }
    }
}

@Composable
fun HomeScreenPreview() {
    com.template.app.ui.theme.TemplateAppTheme {
        HomeContent(
            state = HomeUiState(),
            onRetry = {},
            onRefresh = {},
            onNavigateToQrScanner = {},
            includeFinanceContent = false
        )
    }
}
