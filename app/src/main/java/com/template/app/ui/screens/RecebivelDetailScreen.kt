package com.template.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.template.app.domain.model.RecebivelItem
import com.template.app.domain.model.RecebivelStatus
import com.template.app.domain.model.TimelineEvent
import com.template.app.domain.model.TimelineEventType
import com.template.app.domain.repository.RecebiveisRepository
import com.template.app.ui.components.AppButton
import com.template.app.ui.components.InstallmentTimeline
import com.template.app.ui.components.StatusBadge
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ─── ViewModel ───────────────────────────────────────────────────────────────

sealed interface RecebivelDetailUiState {
    data object Loading : RecebivelDetailUiState
    data class Success(val item: RecebivelItem) : RecebivelDetailUiState
    data class Error(val message: String) : RecebivelDetailUiState
}

@HiltViewModel
class RecebivelDetailViewModel @Inject constructor(
    private val repository: RecebiveisRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val recebivelId: String = checkNotNull(savedStateHandle["id"])

    private val _state = MutableStateFlow<RecebivelDetailUiState>(RecebivelDetailUiState.Loading)
    val state: StateFlow<RecebivelDetailUiState> = _state.asStateFlow()

    init {
        loadDetail()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            try {
                repository.getRecebivelById(recebivelId).collect { item ->
                    _state.value = if (item != null) {
                        RecebivelDetailUiState.Success(item)
                    } else {
                        RecebivelDetailUiState.Error("Recebível não encontrado")
                    }
                }
            } catch (e: Exception) {
                _state.value = RecebivelDetailUiState.Error(e.message ?: "Erro ao carregar detalhe")
            }
        }
    }
}

// ─── Screen ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecebivelDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: RecebivelDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalhe do Recebível",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val currentState = state) {
                is RecebivelDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is RecebivelDetailUiState.Error -> {
                    Text(
                        text = currentState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp)
                    )
                }
                is RecebivelDetailUiState.Success -> {
                    RecebivelDetailContent(
                        item = currentState.item,
                        onRegistrarPagamento = { /* stubbed — payment flow is out of scope */ }
                    )
                }
            }
        }
    }
}

// ─── Content ─────────────────────────────────────────────────────────────────

@Composable
private fun RecebivelDetailContent(
    item: RecebivelItem,
    onRegistrarPagamento: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero header card
        HeroBalanceCard(item = item)

        // Timeline history
        if (item.timeline.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Histórico de Eventos",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    InstallmentTimeline(events = item.timeline)
                }
            }
        }

        // Primary CTA
        AppButton(
            text = "Registrar Pagamento",
            onClick = onRegistrarPagamento,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun HeroBalanceCard(item: RecebivelItem) {
    val progress = if (item.originalAmountValue > 0f) {
        1f - (item.remainingAmountValue / item.originalAmountValue)
    } else {
        1f
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Client info row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.clientName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Due date + status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Vence em: ${item.dueDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
                StatusBadge(status = item.status)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Balance section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "Valor original",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Text(
                        text = item.originalAmount,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Saldo restante",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Text(
                        text = item.remainingAmount,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar (paid portion)
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${"%.0f".format(progress * 100)}% pago",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

private val previewItem = RecebivelItem(
    id = "REC-101",
    clientName = "Acme Corporation",
    clientInitials = "AC",
    description = "Licenciamento de Software ERP - Parcela 2/3",
    originalAmount = "R$ 15.000,00",
    remainingAmount = "R$ 5.000,00",
    originalAmountValue = 15000f,
    remainingAmountValue = 5000f,
    dueDate = "30 Set 2026",
    status = RecebivelStatus.Pendente,
    timeline = listOf(
        TimelineEvent("t1", "Recebível Criado", "Fatura emitida com vencimento para 30/09/2026", "01/09/2026 10:00", TimelineEventType.Created),
        TimelineEvent("t2", "Pagamento Parcial", "Recebido via PIX R$ 10.000,00", "15/09/2026 14:30", TimelineEventType.Payment)
    )
)

@Preview(showBackground = true, name = "Detail Screen — Light")
@Composable
private fun RecebivelDetailContentPreviewLight() {
    com.template.app.ui.theme.TemplateAppTheme(darkTheme = false) {
        RecebivelDetailContent(
            item = previewItem,
            onRegistrarPagamento = {}
        )
    }
}

@Preview(showBackground = true, name = "Detail Screen — Dark")
@Composable
private fun RecebivelDetailContentPreviewDark() {
    com.template.app.ui.theme.TemplateAppTheme(darkTheme = true) {
        RecebivelDetailContent(
            item = previewItem,
            onRegistrarPagamento = {}
        )
    }
}
