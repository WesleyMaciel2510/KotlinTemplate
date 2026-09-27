package com.template.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.template.app.domain.model.PaymentTransaction
import com.template.app.ui.components.EmptyState
import com.template.app.ui.components.ErrorState
import com.template.app.ui.components.ShimmerLoading
import com.template.app.ui.components.charts.DonutChartCanvas
import com.template.app.ui.components.charts.LineChartCanvas

@Composable
fun FinanceContentScreen(
    modifier: Modifier = Modifier,
    viewModel: FinanceViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = modifier) {
        AnimatedContent(
            targetState = state,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "FinanceScreenTransition"
        ) { currentState ->
            when (currentState) {
                is FinanceUiState.Loading -> ShimmerLoading(modifier = Modifier.fillMaxSize())
                is FinanceUiState.Empty -> EmptyState(
                    message = "Nenhum dado financeiro encontrado",
                    modifier = Modifier.fillMaxSize()
                )
                is FinanceUiState.Error -> ErrorState(
                    message = currentState.message,
                    onRetry = { viewModel.onEvent(FinanceUiEvent.Refresh) },
                    modifier = Modifier.fillMaxSize()
                )
                is FinanceUiState.Success -> FinanceSuccessContent(state = currentState)
            }
        }
    }
}

@Composable
private fun FinanceSuccessContent(state: FinanceUiState.Success) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Balance Summary Hero Card Anchor
        item(contentType = "BalanceCard") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Resumo do Saldo Financeiro" },
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Saldo Total Previsto",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = state.netBalance,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Recebido",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                            Text(
                                text = state.totalReceived,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "A Receber",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                            Text(
                                text = state.totalToReceive,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { state.receivedRatio },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                    )
                }
            }
        }

        // Payment Methods Donut Chart
        item(contentType = "PaymentMethodsChart") {
            DonutChartCanvas(
                title = "Distribuição por Meio de Pagamento",
                shares = state.paymentMethodShares
            )
        }

        // Revenue Trend Chart (Reusing LineChartCanvas)
        item(contentType = "RevenueTrendChart") {
            LineChartCanvas(
                title = "Faturamento Mensal (Reais)",
                data = state.revenueTrend,
                lineColor = MaterialTheme.colorScheme.primary
            )
        }

        // Delinquency Trend Chart (Reusing LineChartCanvas)
        item(contentType = "DelinquencyTrendChart") {
            LineChartCanvas(
                title = "Taxa de Inadimplência (%)",
                data = state.delinquencyTrend,
                lineColor = MaterialTheme.colorScheme.error
            )
        }

        // Recent Transactions Header
        item(contentType = "TransactionsHeader") {
            Text(
                text = "Últimos Pagamentos",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Recent Payments List
        items(
            items = state.recentTransactions,
            key = { it.id },
            contentType = { "TransactionItem" }
        ) { transaction ->
            TransactionRowItem(transaction = transaction)
        }
    }
}

@Composable
private fun TransactionRowItem(transaction: PaymentTransaction) {
    val methodIcon = when (transaction.method.lowercase()) {
        "pix" -> Icons.Default.QrCode
        "cartão de crédito", "cartão" -> Icons.Default.CreditCard
        "transferência" -> Icons.Default.AccountBalance
        else -> Icons.Default.ReceiptLong
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = methodIcon,
                    contentDescription = transaction.method,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.clientName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${transaction.method} • ${transaction.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = transaction.amount,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FinanceContentScreenPreview() {
    com.template.app.ui.theme.TemplateAppTheme {
        FinanceContentScreen()
    }
}
