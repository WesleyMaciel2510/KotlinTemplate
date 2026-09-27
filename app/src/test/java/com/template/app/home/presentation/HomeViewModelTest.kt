package com.template.app.home.presentation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class HomeViewModelTest {
    @Test
    fun `starts without fake repository data`() {
        val viewModel = HomeViewModel()

        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.data)
    }

    @Test
    fun `retry and refresh remain safe without a data source`() {
        val viewModel = HomeViewModel()

        viewModel.retry()
        viewModel.refresh()

        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.data)
    }
}
