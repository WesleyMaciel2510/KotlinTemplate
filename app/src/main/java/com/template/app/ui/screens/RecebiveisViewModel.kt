package com.template.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.app.domain.model.KpiMetric
import com.template.app.domain.model.RecebivelFilterOption
import com.template.app.domain.model.RecebivelItem
import com.template.app.domain.model.RecebivelStatus
import com.template.app.domain.repository.RecebiveisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecebiveisViewModel @Inject constructor(
    private val repository: RecebiveisRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow(RecebivelFilterOption.Todos)
    private val _dateRangeText = MutableStateFlow("Período: Set/2026")

    private val _state = MutableStateFlow<RecebiveisUiState>(RecebiveisUiState.Loading)
    val state: StateFlow<RecebiveisUiState> = _state.asStateFlow()

    init {
        loadRecebiveis()
    }

    fun onEvent(event: RecebiveisUiEvent) {
        when (event) {
            is RecebiveisUiEvent.SearchQueryChanged -> {
                _searchQuery.value = event.query
            }
            is RecebiveisUiEvent.FilterSelected -> {
                _selectedFilter.value = event.filter
            }
            is RecebiveisUiEvent.DateRangeApplied -> {
                _dateRangeText.value = "${event.startDate} - ${event.endDate}"
            }
            is RecebiveisUiEvent.Refresh -> loadRecebiveis()
        }
    }

    private fun loadRecebiveis() {
        _state.value = RecebiveisUiState.Loading
        viewModelScope.launch {
            try {
                combine(
                    repository.getRecebiveis(),
                    _searchQuery,
                    _selectedFilter,
                    _dateRangeText
                ) { rawList, query, filter, dateRange ->
                    val filtered = rawList.filter { item ->
                        val matchesQuery = query.isEmpty() ||
                                item.clientName.contains(query, ignoreCase = true) ||
                                item.description.contains(query, ignoreCase = true)

                        val matchesFilter = when (filter) {
                            RecebivelFilterOption.Todos -> true
                            RecebivelFilterOption.Pendentes -> item.status == RecebivelStatus.Pendente
                            RecebivelFilterOption.Pagos -> item.status == RecebivelStatus.Pago
                            RecebivelFilterOption.Vencidos -> item.status == RecebivelStatus.Vencido
                        }

                        matchesQuery && matchesFilter
                    }

                    if (filtered.isEmpty() && query.isEmpty() && filter == RecebivelFilterOption.Todos) {
                        RecebiveisUiState.Empty
                    } else {
                        val totalToReceiveVal = rawList.filter { it.status == RecebivelStatus.Pendente }.sumOf { it.remainingAmountValue.toDouble() }
                        val totalOverdueVal = rawList.filter { it.status == RecebivelStatus.Vencido }.sumOf { it.remainingAmountValue.toDouble() }

                        val kpis = listOf(
                            KpiMetric("k1", "Total a Vencer", "R$ ${"%.2f".format(totalToReceiveVal)}", "3 faturas", isPositive = true),
                            KpiMetric("k2", "Total Vencido", "R$ ${"%.2f".format(totalOverdueVal)}", "2 faturas", isPositive = false)
                        )

                        RecebiveisUiState.Success(
                            totalToReceive = "R$ ${"%.2f".format(totalToReceiveVal)}",
                            totalOverdue = "R$ ${"%.2f".format(totalOverdueVal)}",
                            kpis = kpis,
                            items = filtered,
                            searchQuery = query,
                            selectedFilter = filter,
                            dateRangeText = dateRange
                        )
                    }
                }.collect { newState ->
                    _state.value = newState
                }
            } catch (e: Exception) {
                _state.value = RecebiveisUiState.Error(e.message ?: "Erro ao carregar recebíveis")
            }
        }
    }
}
