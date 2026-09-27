package com.template.app.ui.screens

import com.template.app.data.repository.FakeFinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeFinanceRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeFinanceRepository()
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_isLoading() {
        assertTrue(
            "Initial state should be Loading",
            viewModel.state.value is HomeUiState.Loading
        )
    }

    @Test
    fun afterLoad_stateIsSuccess() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.state.first()
        assertTrue("State should be Success after load", state is HomeUiState.Success)
    }

    @Test
    fun successState_userNameIsNonBlank() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.state.value
        assertTrue("State should be Success", state is HomeUiState.Success)
        val success = state as HomeUiState.Success
        assertTrue(
            "userName must be non-blank",
            success.userName.isNotBlank()
        )
    }

    @Test
    fun successState_containsKpis() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.state.value as HomeUiState.Success
        assertFalse("KPIs should not be empty", state.kpis.isEmpty())
    }

    @Test
    fun successState_containsMonthlyReceipts() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.state.value as HomeUiState.Success
        assertFalse("Monthly receipts should not be empty", state.monthlyReceipts.isEmpty())
    }

    @Test
    fun refresh_reloadsData() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onEvent(HomeUiEvent.Refresh)
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.state.value
        assertTrue("State should be Success after refresh", state is HomeUiState.Success)
    }
}
