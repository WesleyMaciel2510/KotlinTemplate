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
class FinanceViewModel @Inject constructor(
    private val financeRepository: FinanceRepository
) : ViewModel() {

    private val _state = MutableStateFlow<FinanceUiState>(FinanceUiState.Loading)
    val state: StateFlow<FinanceUiState> = _state.asStateFlow()

    init {
        loadFinanceData()
    }

    fun onEvent(event: FinanceUiEvent) {
        when (event) {
            is FinanceUiEvent.Refresh -> loadFinanceData()
        }
    }

    private fun loadFinanceData() {
        _state.value = FinanceUiState.Loading
        viewModelScope.launch {
            try {
                combine(
                    financeRepository.getPaymentMethodShares(),
                    financeRepository.getMonthlyReceiptsTrend(),
                    financeRepository.getDelinquencyTrend(),
                    financeRepository.getRecentTransactions()
                ) { shares, revenue, delinquency, transactions ->
                    if (shares.isEmpty()) {
                        FinanceUiState.Empty
                    } else {
                        FinanceUiState.Success(
                            totalReceived = "R$ 116.400,00",
                            totalToReceive = "R$ 32.100,00",
                            netBalance = "R$ 148.500,00",
                            receivedRatio = 0.784f,
                            paymentMethodShares = shares,
                            revenueTrend = revenue,
                            delinquencyTrend = delinquency,
                            recentTransactions = transactions
                        )
                    }
                }.collect { newState ->
                    _state.value = newState
                }
            } catch (e: Exception) {
                _state.value = FinanceUiState.Error(e.message ?: "Erro ao carregar dados financeiros")
            }
        }
    }
}
