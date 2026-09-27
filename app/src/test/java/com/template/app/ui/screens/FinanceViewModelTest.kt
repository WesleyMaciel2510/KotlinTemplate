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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FinanceViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeFinanceRepository
    private lateinit var viewModel: FinanceViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeFinanceRepository()
        viewModel = FinanceViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadFinanceData_emitsSuccessStateWithData() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()
        val currentState = viewModel.state.first()
        assertTrue("State should be Success", currentState is FinanceUiState.Success)
    }
}
