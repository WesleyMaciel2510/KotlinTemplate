package com.template.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.app.domain.usecase.GetExampleItemsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.Result

class HomeViewModel(
    private val getExampleItemsUseCase: GetExampleItemsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.LoadItems -> loadItems()
            is HomeUiEvent.ItemClicked -> { /* Handle item click if needed */ }
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = getExampleItemsUseCase()
            result.fold(
                onSuccess = { items ->
                    _state.value = _state.value.copy(isLoading = false, items = items)
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(isLoading = false, error = error.message)
                }
            )
        }
    }
}