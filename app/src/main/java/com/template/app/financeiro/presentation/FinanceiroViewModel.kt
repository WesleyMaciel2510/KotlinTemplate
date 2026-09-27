package com.template.app.financeiro.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.app.financeiro.data.FinanceiroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FinanceiroViewModel @Inject constructor(
    private val repository: FinanceiroRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FinanceiroUiState())
    val uiState: StateFlow<FinanceiroUiState> = _uiState.asStateFlow()

    init {
        load(initial = true)
    }

    fun retry() {
        load(initial = true)
    }

    fun refresh() {
        if (_uiState.value.isLoading || _uiState.value.isRefreshing) return
        load(initial = false)
    }

    private fun load(initial: Boolean) {
        if (initial) {
            _uiState.update {
                it.copy(isLoading = true, isRefreshing = false, errorMessage = null)
            }
        } else {
            _uiState.update {
                it.copy(isRefreshing = true, refreshErrorMessage = null)
            }
        }

        viewModelScope.launch {
            runCatching { repository.getFinanceiroSummary() }
                .onSuccess { summary ->
                    _uiState.value = FinanceiroUiState(data = summary, isLoading = false)
                }
                .onFailure { error ->
                    val message = error.message ?: "Não foi possível carregar suas finanças."
                    _uiState.update {
                        if (initial) {
                            it.copy(isLoading = false, errorMessage = message)
                        } else {
                            it.copy(isRefreshing = false, refreshErrorMessage = message)
                        }
                    }
                }
        }
    }
}
