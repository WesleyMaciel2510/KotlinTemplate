package com.template.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.app.domain.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val financeRepository: FinanceRepository
) : ViewModel() {

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        loadDashboardData()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.Refresh -> loadDashboardData()
        }
    }

    private fun loadDashboardData() {
        _state.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                combine(
                    financeRepository.getUserName(),
                    financeRepository.getKpiMetrics(),
                    financeRepository.getMonthlyReceiptsTrend(),
                    financeRepository.getTopClients(),
                    financeRepository.getRecentActivities()
                ) { userName, kpis, receipts, clients, activities ->
                    if (kpis.isEmpty()) {
                        HomeUiState.Empty
                    } else {
                        HomeUiState.Success(
                            userName = userName,
                            kpis = kpis,
                            monthlyReceipts = receipts,
                            topClients = clients,
                            recentActivities = activities
                        )
                    }
                }.collect { newState ->
                    _state.value = newState
                }
            } catch (e: Exception) {
                _state.value = HomeUiState.Error(e.message ?: "Erro ao carregar dados do painel")
            }
        }
    }
}
