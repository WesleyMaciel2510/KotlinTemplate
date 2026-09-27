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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.template.app.domain.model.KpiMetric
import com.template.app.domain.model.MonthlyTrendPoint
import com.template.app.domain.model.TopClient
import com.template.app.ui.components.AppButton
import com.template.app.ui.components.EmptyState
import com.template.app.ui.components.ErrorState
import com.template.app.ui.components.KpiCard
import com.template.app.ui.components.ShimmerLoading
import com.template.app.ui.components.charts.LineChartCanvas
import com.template.app.data.preferences.ThemePreference
import com.template.app.ui.theme.TemplateAppTheme

// ──────────────────────────────────────────────────────────────
// Screen entry point
// ──────────────────────────────────────────────────────────────

@Composable
fun HomeContentScreen(
    onNavigateToRecebiveis: () -> Unit = {},
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
                    onNavigateToRecebiveis = onNavigateToRecebiveis,
                    onRefresh = { viewModel.onEvent(HomeUiEvent.Refresh) }
                )
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────
// Success layout
// ──────────────────────────────────────────────────────────────

@Composable
private fun HomeSuccessContent(
    state: HomeUiState.Success,
    onNavigateToRecebiveis: () -> Unit,
    onRefresh: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Personalized header
        item(contentType = "Header") {
            HomeHeader(
                userName = state.userName,
                onRefresh = onRefresh,
                onNotificationClick = {}
            )
        }

        // Hero balance card
        item(contentType = "HeroBalance") {
            val firstKpi = state.kpis.firstOrNull()
            HeroBalanceCard(
                totalToReceive = firstKpi?.value ?: "R$ 0,00",
                trendLabel = firstKpi?.subtitle ?: "",
                isPositiveTrend = firstKpi?.isPositive ?: true
            )
        }

        // Quick actions row
        item(contentType = "QuickActions") {
            QuickActionsRow(
                onNovoRecebivel = onNavigateToRecebiveis,
                onRegistrarPagamento = {},
            )
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
                        key = { "kpi-${it.id}" },
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
            key = { "client-${it.id}" },
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
            key = { "activity-${it.id}" },
            contentType = { "ActivityItem" }
        ) { activity ->
            ActivityRowItem(activity = activity)
        }
    }
}

// ──────────────────────────────────────────────────────────────
// HomeHeader (task 2.1)
// ──────────────────────────────────────────────────────────────

/**
 * Personalized header with circular initials avatar, greeting text,
 * notification bell and tinted refresh icon button.
 */
@Composable
private fun HomeHeader(
    userName: String,
    onRefresh: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Derive up to two initials from the userName (e.g. "João Silva" → "JS")
    val initials = userName
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }
        .ifEmpty { "U" }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circular initials avatar
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Greeting column
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Olá, $userName",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Veja seu painel financeiro",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Notification bell (decorative for now)
        IconButton(onClick = onNotificationClick) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notificações",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Tinted refresh button
        IconButton(onClick = onRefresh) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Atualizar",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// ──────────────────────────────────────────────────────────────
// HeroBalanceCard (task 2.2)
// ──────────────────────────────────────────────────────────────

/**
 * Full-width hero card with Primary container color showing the total
 * receivable balance and a month-over-month trend indicator.
 */
@Composable
private fun HeroBalanceCard(
    totalToReceive: String,
    trendLabel: String,
    isPositiveTrend: Boolean,
    modifier: Modifier = Modifier
) {
    val trendIcon = if (isPositiveTrend) {
        Icons.AutoMirrored.Filled.TrendingUp
    } else {
        Icons.AutoMirrored.Filled.TrendingDown
    }
    val trendTint = if (isPositiveTrend) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.errorContainer
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Total a Receber",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = totalToReceive,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            if (trendLabel.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = trendIcon,
                        contentDescription = if (isPositiveTrend) "Tendência positiva" else "Tendência negativa",
                        tint = trendTint,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = trendLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────
// QuickActionsRow (task 2.3)
// ──────────────────────────────────────────────────────────────

/**
 * Two filled primary action buttons ("Novo Recebível", "Registrar Pagamento")
 * and an icon-only QR Scanner secondary button.
 */
@Composable
private fun QuickActionsRow(
    onNovoRecebivel: () -> Unit,
    onRegistrarPagamento: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // "Novo Recebível" — primary filled button with Add icon
        AppButton(
            text = "Novo Recebível",
            onClick = onNovoRecebivel,
            modifier = Modifier.weight(1f)
        )

        // "Registrar Pagamento" — primary filled button with Payment icon
        AppButton(
            text = "Registrar Pagamento",
            onClick = onRegistrarPagamento,
            modifier = Modifier.weight(1f)
        )
    }
}

// ──────────────────────────────────────────────────────────────
// ActivityRowItem — elevated card with trailing chevron (task 2.4)
// ──────────────────────────────────────────────────────────────

@Composable
private fun ActivityRowItem(activity: ActivityEvent) {
    val (icon, tint) = when (activity.eventType) {
        ActivityEventType.PaymentReceived -> Icons.Default.AttachMoney to MaterialTheme.colorScheme.primary
        ActivityEventType.InvoiceSent -> Icons.Default.Description to MaterialTheme.colorScheme.secondary
        ActivityEventType.OverdueAlert -> Icons.Default.AddAlert to MaterialTheme.colorScheme.error
        ActivityEventType.ClientAdded -> Icons.Default.PersonAdd to MaterialTheme.colorScheme.tertiary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Event type icon in tinted circle
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
            Spacer(modifier = Modifier.width(4.dp))
            // Trailing chevron
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// ──────────────────────────────────────────────────────────────
// TopClientRowItem (unchanged)
// ──────────────────────────────────────────────────────────────

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

// ──────────────────────────────────────────────────────────────
// Previews (task 3.4)
// ──────────────────────────────────────────────────────────────

private val previewSuccessState = HomeUiState.Success(
    userName = "João Silva",
    kpis = listOf(
        KpiMetric("1", "Total Recebíveis", "R$ 148.500,00", "+12.4% este mês", isPositive = true),
        KpiMetric("2", "Valor Pendente", "R$ 32.100,00", "8 faturas em aberto", isPositive = false)
    ),
    monthlyReceipts = listOf(
        MonthlyTrendPoint("Jan", 65f, 50f),
        MonthlyTrendPoint("Fev", 78f, 60f),
        MonthlyTrendPoint("Mar", 92f, 75f)
    ),
    topClients = listOf(
        TopClient("1", "Acme Corporation", "AC", "R$ 45.200,00")
    ),
    recentActivities = listOf(
        ActivityEvent("1", "Pagamento Recebido", "Acme Corp pagou fatura #1042", "Há 15 min", ActivityEventType.PaymentReceived),
        ActivityEvent("2", "Alerta de Vencimento", "Fatura #1038 vence em 2 dias", "Há 4 horas", ActivityEventType.OverdueAlert)
    )
)

@Preview(showBackground = true, name = "HomeHeader – Light")
@Composable
private fun HomeHeaderPreviewLight() {
    TemplateAppTheme(themePreference = ThemePreference.Light) {
        HomeHeader(userName = "João Silva", onRefresh = {}, onNotificationClick = {})
    }
}

@Preview(showBackground = true, name = "HomeHeader – Dark")
@Composable
private fun HomeHeaderPreviewDark() {
    TemplateAppTheme(themePreference = ThemePreference.Dark) {
        HomeHeader(userName = "João Silva", onRefresh = {}, onNotificationClick = {})
    }
}

@Preview(showBackground = true, name = "HeroBalanceCard – Light")
@Composable
private fun HeroBalanceCardPreviewLight() {
    TemplateAppTheme(themePreference = ThemePreference.Light) {
        HeroBalanceCard(
            totalToReceive = "R$ 148.500,00",
            trendLabel = "+12.4% este mês",
            isPositiveTrend = true
        )
    }
}

@Preview(showBackground = true, name = "HeroBalanceCard – Dark")
@Composable
private fun HeroBalanceCardPreviewDark() {
    TemplateAppTheme(themePreference = ThemePreference.Dark) {
        HeroBalanceCard(
            totalToReceive = "R$ 148.500,00",
            trendLabel = "+12.4% este mês",
            isPositiveTrend = true
        )
    }
}

@Preview(showBackground = true, name = "QuickActionsRow – Light")
@Composable
private fun QuickActionsRowPreviewLight() {
    TemplateAppTheme(themePreference = ThemePreference.Light) {
        QuickActionsRow(onNovoRecebivel = {}, onRegistrarPagamento = {})
    }
}

@Preview(showBackground = true, name = "ActivityRowItem – Light")
@Composable
private fun ActivityRowItemPreviewLight() {
    TemplateAppTheme(themePreference = ThemePreference.Light) {
        ActivityRowItem(
            activity = ActivityEvent(
                "1", "Pagamento Recebido", "Acme Corp pagou fatura #1042", "Há 15 min",
                ActivityEventType.PaymentReceived
            )
        )
    }
}

@Preview(showBackground = true, name = "HomeSuccessContent – Light")
@Composable
private fun HomeSuccessContentPreviewLight() {
    TemplateAppTheme(themePreference = ThemePreference.Light) {
        HomeSuccessContent(
            state = previewSuccessState,
            onNavigateToRecebiveis = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true, name = "HomeSuccessContent – Dark")
@Composable
private fun HomeSuccessContentPreviewDark() {
    TemplateAppTheme(themePreference = ThemePreference.Dark) {
        HomeSuccessContent(
            state = previewSuccessState,
            onNavigateToRecebiveis = {},
            onRefresh = {}
        )
    }
}
