package com.template.app.qrcode

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [BarcodeAnalyzer] debounce logic.
 *
 * Because BarcodeAnalyzer depends on ML Kit's ImageProxy at analysis time, we test the
 * debounce bookkeeping directly via a helper that mimics the same condition — same value
 * within the debounce window should not increment the call count.
 */
class BarcodeAnalyzerDebounceTest {

    /**
     * Simulates the debounce state machine extracted from BarcodeAnalyzer so it can be
     * tested without a real camera frame.
     */
    private class DebounceStateMachine(
        private val debounceMs: Long = 2_000L,
        private val onResult: (String) -> Unit,
    ) {
        private var lastValue: String? = null
        private var lastTimestampMs: Long = 0L

        fun process(raw: String, nowMs: Long) {
            if (raw == lastValue && nowMs - lastTimestampMs < debounceMs) return
            lastValue = raw
            lastTimestampMs = nowMs
            onResult(raw)
        }
    }

    @Test
    fun `same value within debounce window is suppressed`() {
        val results = mutableListOf<String>()
        val machine = DebounceStateMachine(debounceMs = 2_000L) { results.add(it) }

        machine.process("https://example.com", nowMs = 0)
        machine.process("https://example.com", nowMs = 500)   // within 2 s → suppressed
        machine.process("https://example.com", nowMs = 1_500) // within 2 s → suppressed

        assertEquals(1, results.size)
        assertEquals("https://example.com", results[0])
    }

    @Test
    fun `same value after debounce window emits again`() {
        val results = mutableListOf<String>()
        val machine = DebounceStateMachine(debounceMs = 2_000L) { results.add(it) }

        machine.process("https://example.com", nowMs = 0)
        machine.process("https://example.com", nowMs = 2_001) // past 2 s → emitted

        assertEquals(2, results.size)
    }

    @Test
    fun `different values are always emitted regardless of debounce`() {
        val results = mutableListOf<String>()
        val machine = DebounceStateMachine(debounceMs = 2_000L) { results.add(it) }

        machine.process("VALUE_A", nowMs = 0)
        machine.process("VALUE_B", nowMs = 100) // different value → always emitted

        assertEquals(2, results.size)
        assertEquals("VALUE_A", results[0])
        assertEquals("VALUE_B", results[1])
    }

    @Test
    fun `first detection is always emitted`() {
        val results = mutableListOf<String>()
        val machine = DebounceStateMachine(debounceMs = 2_000L) { results.add(it) }

        machine.process("QR_FIRST", nowMs = 0)

        assertEquals(1, results.size)
    }
}
