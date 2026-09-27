package com.template.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.template.app.domain.model.RecebivelItem
import com.template.app.domain.model.RecebivelStatus
import com.template.app.ui.components.AppTextField
import com.template.app.ui.components.DateRangePickerSheet
import com.template.app.ui.components.EmptyState
import com.template.app.ui.components.ErrorState
import com.template.app.ui.components.FilterChipRow
import com.template.app.ui.components.KpiCard
import com.template.app.ui.components.ShimmerLoading
import com.template.app.ui.components.StatusBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecebiveisListScreen(
    onRecebivelClick: (String) -> Unit,
    viewModel: RecebiveisViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDatePickerSheet by remember { mutableStateOf(false) }

    if (showDatePickerSheet) {
        DateRangePickerSheet(
            onDismissRequest = { showDatePickerSheet = false },
            onApplyDateRange = { start, end ->
                viewModel.onEvent(RecebiveisUiEvent.DateRangeApplied(start, end))
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = state,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "RecebiveisScreenTransition"
        ) { currentState ->
            when (currentState) {
                is RecebiveisUiState.Loading -> ShimmerLoading(modifier = Modifier.fillMaxSize())
                is RecebiveisUiState.Empty -> EmptyState(
                    message = "Nenhum recebível cadastrado",
                    modifier = Modifier.fillMaxSize()
                )
                is RecebiveisUiState.Error -> ErrorState(
                    message = currentState.message,
                    onRetry = { viewModel.onEvent(RecebiveisUiEvent.Refresh) },
                    modifier = Modifier.fillMaxSize()
                )
                is RecebiveisUiState.Success -> RecebiveisSuccessContent(
                    state = currentState,
                    onEvent = viewModel::onEvent,
                    onRecebivelClick = onRecebivelClick,
                    onOpenDatePicker = { showDatePickerSheet = true }
                )
            }
        }
    }
}

@Composable
private fun RecebiveisSuccessContent(
    state: RecebiveisUiState.Success,
    onEvent: (RecebiveisUiEvent) -> Unit,
    onRecebivelClick: (String) -> Unit,
    onOpenDatePicker: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // KPI Overview Row
        item(contentType = "KpiRow") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KpiCard(
                    title = "Total a Vencer",
                    value = state.totalToReceive,
                    sparklineData = listOf(10f, 20f, 15f, 30f, 45f),
                    isPositive = true,
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    title = "Total Vencido",
                    value = state.totalOverdue,
                    sparklineData = listOf(50f, 40f, 60f, 35f, 20f),
                    isPositive = false,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Search Field & Date Trigger
        item(contentType = "SearchAndFilter") {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppTextField(
                        value = state.searchQuery,
                        onValueChange = { onEvent(RecebiveisUiEvent.SearchQueryChanged(it)) },
                        label = "Buscar por cliente ou descrição",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onOpenDatePicker) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Filtrar por data",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.dateRangeText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(8.dp))
                FilterChipRow(
                    selectedOption = state.selectedFilter,
                    onOptionSelected = { onEvent(RecebiveisUiEvent.FilterSelected(it)) }
                )
            }
        }

        // Receivables List Items
        items(
            items = state.items,
            key = { it.id },
            contentType = { "RecebivelItem" }
        ) { item ->
            RecebivelRowCard(
                item = item,
                onClick = { onRecebivelClick(item.id) }
            )
        }
    }
}

@Composable
private fun RecebivelRowCard(
    item: RecebivelItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.clientInitials,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.clientName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Vence em: ${item.dueDate}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = item.remainingAmount,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                StatusBadge(status = item.status)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecebiveisListScreenPreview() {
    com.template.app.ui.theme.TemplateAppTheme {
        RecebiveisListScreen(onRecebivelClick = {})
    }
}
