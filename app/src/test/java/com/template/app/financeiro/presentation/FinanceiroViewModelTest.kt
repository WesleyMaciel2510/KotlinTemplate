package com.template.app.financeiro.presentation

import com.template.app.CoroutineTestRule
import com.template.app.financeiro.data.FakeFinanceiroRepository
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class FinanceiroViewModelTest {
    @get:Rule val mainRule = CoroutineTestRule()

    @Test
    fun `loads financial summary successfully`() = runTest {
        val viewModel = FinanceiroViewModel(FakeFinanceiroRepository())
        advanceUntilIdle()

        assertEquals(FakeFinanceiroRepository.defaultSummary, viewModel.uiState.value.data)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `retains data when refresh fails`() = runTest {
        val repository = FakeFinanceiroRepository()
        val viewModel = FinanceiroViewModel(repository)
        advanceUntilIdle()
        repository.setNextResult(Result.failure(IllegalStateException("offline")))

        viewModel.refresh()
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.data)
        assertEquals("offline", viewModel.uiState.value.refreshErrorMessage)
        assertFalse(viewModel.uiState.value.isRefreshing)
    }

    @Test
    fun `retry clears initial error after failure`() = runTest {
        val repository = FakeFinanceiroRepository(Result.failure(IllegalStateException("offline")))
        val viewModel = FinanceiroViewModel(repository)
        advanceUntilIdle()
        repository.setNextResult(Result.success(FakeFinanceiroRepository.defaultSummary))

        viewModel.retry()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.errorMessage == null)
        assertNotNull(viewModel.uiState.value.data)
    }
}
