package com.template.app.financeiro.data

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class FakeFinanceiroRepositoryTest {
    @Test
    fun `returns deterministic default summary`() = runTest {
        assertEquals(FakeFinanceiroRepository.defaultSummary, FakeFinanceiroRepository().getFinanceiroSummary())
    }

    @Test
    fun `returns configured empty summary`() = runTest {
        val empty = FakeFinanceiroRepository.defaultSummary.copy(entries = emptyList())
        val repository = FakeFinanceiroRepository(Result.success(empty))

        assertEquals(empty, repository.getFinanceiroSummary())
    }

    @Test
    fun `throws configured failure`() = runTest {
        val repository = FakeFinanceiroRepository(Result.failure(IllegalStateException("offline")))

        assertThrows(IllegalStateException::class.java) { runTest { repository.getFinanceiroSummary() } }
    }
}
