package com.template.app.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class AppColorsTest {
    @Test
    fun lightAndDarkTokensKeepIndependentStatusFamilies() {
        assertNotEquals(AppColors.light.pendingContainer, AppColors.light.paidContainer)
        assertNotEquals(AppColors.light.paidContainer, AppColors.light.overdueContainer)
        assertNotEquals(AppColors.dark.pendingContainer, AppColors.dark.paidContainer)
        assertNotEquals(AppColors.dark.paidContainer, AppColors.dark.overdueContainer)
    }

    @Test
    fun chartPaletteIsOrderedWithFiveStableEntriesPerTheme() {
        assertEquals(5, AppColors.light.chartPalette.size)
        assertEquals(5, AppColors.dark.chartPalette.size)
        assertEquals(AppColors.light.chartPalette[0], AppColors.light.chartPalette[0])
        assertEquals(AppColors.dark.chartPalette[0], AppColors.dark.chartPalette[0])
    }
}
