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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.template.app.domain.model.ActivityEvent
import com.template.app.domain.model.ActivityEventType
import com.template.app.domain.model.TopClient
import com.template.app.ui.components.EmptyState
import com.template.app.ui.components.ErrorState
import com.template.app.ui.components.KpiCard
import com.template.app.ui.components.ShimmerLoading
import com.template.app.ui.components.charts.LineChartCanvas

@Composable
fun HomeContentScreen(
    onNavigateToQrScanner: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = state,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "HomeScreenTransition"
        ) { currentState ->
            when (currentState) {
                is HomeUiState.Loading -> ShimmerLoading(modifier = Modifier.fillMaxSize())
                is HomeUiState.Empty -> EmptyState(
                    message = "Nenhum dado financeiro disponível no momento",
                    modifier = Modifier.fillMaxSize()
                )
                is HomeUiState.Error -> ErrorState(
                    message = currentState.message,
                    onRetry = { viewModel.onEvent(HomeUiEvent.Refresh) },
                    modifier = Modifier.fillMaxSize()
                )
                is HomeUiState.Success -> HomeSuccessContent(
                    state = currentState,
                    onNavigateToQrScanner = onNavigateToQrScanner
                )
            }
        }
    }
}

@Composable
private fun HomeSuccessContent(
    state: HomeUiState.Success,
    onNavigateToQrScanner: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Quick Action Bar
        item(contentType = "QuickAction") {
            OutlinedButton(
                onClick = onNavigateToQrScanner,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "Escanear QR Code",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Abrir Scanner de QR Code",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        // Horizontal KPI Cards
        item(contentType = "KpiRow") {
            Column {
                Text(
                    text = "Indicadores Principais",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = state.kpis,
                        key = { it.id },
                        contentType = { "KpiCard" }
                    ) { kpi ->
                        KpiCard(
                            title = kpi.title,
                            value = kpi.value,
                            subtitle = kpi.subtitle,
                            isPositive = kpi.isPositive,
                            modifier = Modifier.width(200.dp)
                        )
                    }
                }
            }
        }

        // Receipts Trend Canvas Chart
        item(contentType = "ReceiptsChart") {
            LineChartCanvas(
                title = "Evolução de Recebimentos Mensais",
                data = state.monthlyReceipts,
                lineColor = MaterialTheme.colorScheme.primary
            )
        }

        // Top Clients Ranking
        item(contentType = "TopClientsHeader") {
            Text(
                text = "Principais Clientes",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(
            items = state.topClients,
            key = { it.id },
            contentType = { "TopClientItem" }
        ) { client ->
            TopClientRowItem(client = client)
        }

        // Recent Activity Feed
        item(contentType = "ActivityHeader") {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Atividades Recentes",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(
            items = state.recentActivities,
            key = { it.id },
            contentType = { "ActivityItem" }
        ) { activity ->
            ActivityRowItem(activity = activity)
        }
    }
}

@Composable
private fun TopClientRowItem(client: TopClient) {
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
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = client.initials,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = client.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = client.totalBilled,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ActivityRowItem(activity: ActivityEvent) {
    val (icon, tint) = when (activity.eventType) {
        ActivityEventType.PaymentReceived -> Icons.Default.AttachMoney to MaterialTheme.colorScheme.primary
        ActivityEventType.InvoiceSent -> Icons.Default.Description to MaterialTheme.colorScheme.secondary
        ActivityEventType.OverdueAlert -> Icons.Default.AddAlert to MaterialTheme.colorScheme.error
        ActivityEventType.ClientAdded -> Icons.Default.PersonAdd to MaterialTheme.colorScheme.tertiary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = activity.title,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = activity.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = activity.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = activity.timeAgo,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentScreenPreview() {
    com.template.app.ui.theme.TemplateAppTheme {
        HomeContentScreen(onNavigateToQrScanner = {})
    }
}