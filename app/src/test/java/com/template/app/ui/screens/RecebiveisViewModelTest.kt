package com.template.app.ui.screens

import com.template.app.data.repository.FakeRecebiveisRepository
import com.template.app.domain.model.RecebivelFilterOption
import com.template.app.domain.model.RecebivelStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecebiveisViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeRecebiveisRepository
    private lateinit var viewModel: RecebiveisViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeRecebiveisRepository()
        viewModel = RecebiveisViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_isLoading() {
        assertTrue(
            "Initial state should be Loading",
            viewModel.state.value is RecebiveisUiState.Loading
        )
    }

    @Test
    fun afterLoad_stateIsSuccess() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.state.first()
        assertTrue("State should be Success after load", state is RecebiveisUiState.Success)
    }

    @Test
    fun filterByVencidos_showsOnlyVencidoItems() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(RecebiveisUiEvent.FilterSelected(RecebivelFilterOption.Vencidos))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue("State should be Success", state is RecebiveisUiState.Success)
        val successState = state as RecebiveisUiState.Success
        assertTrue(
            "All items must have Vencido status",
            successState.items.all { it.status == RecebivelStatus.Vencido }
        )
    }

    @Test
    fun filterByPagos_showsOnlyPagosItems() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(RecebiveisUiEvent.FilterSelected(RecebivelFilterOption.Pagos))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue("State should be Success", state is RecebiveisUiState.Success)
        val successState = state as RecebiveisUiState.Success
        assertTrue(
            "All items must have Pago status",
            successState.items.all { it.status == RecebivelStatus.Pago }
        )
    }

    @Test
    fun filterByPendentes_showsOnlyPendentesItems() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(RecebiveisUiEvent.FilterSelected(RecebivelFilterOption.Pendentes))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue("State should be Success", state is RecebiveisUiState.Success)
        val successState = state as RecebiveisUiState.Success
        assertTrue(
            "All items must have Pendente status",
            successState.items.all { it.status == RecebivelStatus.Pendente }
        )
    }

    @Test
    fun searchByClientName_filtersMatchingItems() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(RecebiveisUiEvent.SearchQueryChanged("Acme"))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue("State should be Success", state is RecebiveisUiState.Success)
        val successState = state as RecebiveisUiState.Success
        assertTrue(
            "All items must match search query",
            successState.items.all {
                it.clientName.contains("Acme", ignoreCase = true) ||
                    it.description.contains("Acme", ignoreCase = true)
            }
        )
    }

    @Test
    fun searchByDescription_filtersMatchingItems() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(RecebiveisUiEvent.SearchQueryChanged("AWS"))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue("State should be Success", state is RecebiveisUiState.Success)
        val successState = state as RecebiveisUiState.Success
        assertTrue(
            "Items should match description query",
            successState.items.all {
                it.clientName.contains("AWS", ignoreCase = true) ||
                    it.description.contains("AWS", ignoreCase = true)
            }
        )
    }

    @Test
    fun clearSearch_restoredAllItems() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()

        // Get full list size
        val initialState = viewModel.state.value as RecebiveisUiState.Success
        val totalItems = initialState.items.size

        // Apply search
        viewModel.onEvent(RecebiveisUiEvent.SearchQueryChanged("Acme"))
        testDispatcher.scheduler.advanceUntilIdle()

        // Clear search
        viewModel.onEvent(RecebiveisUiEvent.SearchQueryChanged(""))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue("State should be Success", state is RecebiveisUiState.Success)
        val successState = state as RecebiveisUiState.Success
        assertEquals(
            "Cleared search should restore all items",
            totalItems,
            successState.items.size
        )
    }

    @Test
    fun filterTodos_showsAllItems() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()

        // Apply a filter first
        viewModel.onEvent(RecebiveisUiEvent.FilterSelected(RecebivelFilterOption.Pagos))
        testDispatcher.scheduler.advanceUntilIdle()

        // Reset to Todos
        viewModel.onEvent(RecebiveisUiEvent.FilterSelected(RecebivelFilterOption.Todos))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value as RecebiveisUiState.Success
        assertEquals(
            "Todos filter should show all 5 items from FakeRepository",
            5,
            state.items.size
        )
    }

    @Test
    fun applyDateRange_updatesDateRangeText() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(RecebiveisUiEvent.DateRangeApplied("01/10/2026", "31/10/2026"))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue("State should be Success", state is RecebiveisUiState.Success)
        val successState = state as RecebiveisUiState.Success
        assertEquals(
            "Date range text should reflect applied range",
            "01/10/2026 - 31/10/2026",
            successState.dateRangeText
        )
    }

    @Test
    fun selectedFilter_isReflectedInState() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(RecebiveisUiEvent.FilterSelected(RecebivelFilterOption.Vencidos))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value as RecebiveisUiState.Success
        assertEquals(RecebivelFilterOption.Vencidos, state.selectedFilter)
    }

    @Test
    fun searchQuery_isReflectedInState() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(RecebiveisUiEvent.SearchQueryChanged("Tech"))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value as RecebiveisUiState.Success
        assertEquals("Tech", state.searchQuery)
    }
}
