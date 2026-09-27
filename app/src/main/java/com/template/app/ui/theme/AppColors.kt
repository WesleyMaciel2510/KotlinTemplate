package com.template.app.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Steel Blue is the brand anchor. Semantic status colors stay independent.
private object Palette {
    val Blue10 = Color(0xFF001D33)
    val Blue20 = Color(0xFF1B334B)
    val Blue30 = Color(0xFF344D65)
    val Blue80 = Color(0xFFB0CCE8)
    val Blue90 = Color(0xFFD1E4F7)

    val Teal10 = Color(0xFF003735)
    val Teal20 = Color(0xFF23615E)
    val Teal30 = Color(0xFF1A4F4C)
    val Teal80 = Color(0xFF86D5CD)
    val Teal90 = Color(0xFFB9E6E0)

    val Amber10 = Color(0xFF2A1800)
    val Amber20 = Color(0xFF472A00)
    val Amber30 = Color(0xFF654000)
    val Amber40 = Color(0xFF865300)
    val Amber80 = Color(0xFFFFB95C)
    val Amber90 = Color(0xFFFFDEA6)

    val Green10 = Color(0xFF00210D)
    val Green30 = Color(0xFF005326)
    val Green40 = Color(0xFF1B6B3A)
    val Green80 = Color(0xFF82D995)
    val Green90 = Color(0xFFB6F2C3)

    val Error10 = Color(0xFF410002)
    val Error40 = Color(0xFFBA1A1A)
    val Error80 = Color(0xFFFFB4AB)
    val Error90 = Color(0xFFFFDAD6)

    val Neutral4 = Color(0xFF141C1F)
    val Neutral6 = Color(0xFF191D1E)
    val Neutral12 = Color(0xFF1A1C1E)
    val Neutral17 = Color(0xFF27282A)
    val Neutral22 = Color(0xFF30373A)
    val Neutral92 = Color(0xFFE0E2E5)
    val Neutral94 = Color(0xFFE8EAED)
    val Neutral96 = Color(0xFFF0F2F5)
    val Neutral98 = Color(0xFFF8F9FA)
    val Neutral100 = Color(0xFFFFFFFF)
    val NeutralVar30 = Color(0xFF44474A)
    val NeutralVar50 = Color(0xFF747679)
    val NeutralVar60 = Color(0xFF8E9093)
    val NeutralVar80 = Color(0xFFC4C6C9)
    val NeutralVar90 = Color(0xFFE2E2E5)
    val ChartSlate = Color(0xFF5B6470)
    val ChartBlue = Color(0xFF3D7EA6)
}

@Immutable
data class AppColorTokens(
    val pendingContainer: Color,
    val onPendingContainer: Color,
    val pending: Color,
    val onPending: Color,
    val paidContainer: Color,
    val onPaidContainer: Color,
    val paid: Color,
    val onPaid: Color,
    val overdueContainer: Color,
    val onOverdueContainer: Color,
    val overdue: Color,
    val onOverdue: Color,
    val chartPalette: List<Color>
)

object AppColors {
    val light = AppColorTokens(
        pendingContainer = Palette.Amber90,
        onPendingContainer = Palette.Amber10,
        pending = Palette.Amber40,
        onPending = Palette.Neutral100,
        paidContainer = Palette.Green90,
        onPaidContainer = Palette.Green10,
        paid = Palette.Green40,
        onPaid = Palette.Neutral100,
        overdueContainer = Palette.Error90,
        onOverdueContainer = Palette.Error10,
        overdue = Palette.Error40,
        onOverdue = Palette.Neutral100,
        chartPalette = listOf(Palette.Blue20, Palette.Teal20, Palette.Amber40, Palette.ChartSlate, Palette.ChartBlue)
    )

    val dark = AppColorTokens(
        pendingContainer = Palette.Amber30,
        onPendingContainer = Palette.Amber90,
        pending = Palette.Amber80,
        onPending = Palette.Amber20,
        paidContainer = Palette.Green30,
        onPaidContainer = Palette.Green90,
        paid = Palette.Green80,
        onPaid = Palette.Green10,
        overdueContainer = Palette.Error40,
        onOverdueContainer = Palette.Error90,
        overdue = Palette.Error80,
        onOverdue = Palette.Error10,
        chartPalette = listOf(Palette.Blue80, Palette.Teal80, Palette.Amber80, Palette.NeutralVar80, Palette.ChartBlue)
    )
}

val LocalAppColors = staticCompositionLocalOf { AppColors.light }

val appLightColorScheme = lightColorScheme(
    primary = Palette.Blue20,
    onPrimary = Palette.Neutral100,
    primaryContainer = Palette.Blue90,
    onPrimaryContainer = Palette.Blue10,
    secondary = Palette.Teal20,
    onSecondary = Palette.Neutral100,
    secondaryContainer = Palette.Teal90,
    onSecondaryContainer = Palette.Teal10,
    tertiary = Palette.Amber40,
    onTertiary = Palette.Neutral100,
    tertiaryContainer = Palette.Amber90,
    onTertiaryContainer = Palette.Amber10,
    error = Palette.Error40,
    onError = Palette.Neutral100,
    errorContainer = Palette.Error90,
    onErrorContainer = Palette.Error10,
    background = Palette.Neutral98,
    onBackground = Palette.Neutral12,
    surface = Palette.Neutral98,
    onSurface = Palette.Neutral12,
    surfaceVariant = Palette.Neutral94,
    onSurfaceVariant = Palette.NeutralVar30,
    surfaceContainer = Palette.Neutral94,
    surfaceContainerHigh = Palette.Neutral92,
    surfaceContainerHighest = Palette.Neutral92,
    surfaceContainerLow = Palette.Neutral96,
    surfaceContainerLowest = Palette.Neutral100,
    outline = Palette.NeutralVar50,
    outlineVariant = Palette.NeutralVar80,
    scrim = Palette.Neutral4
)

val appDarkColorScheme = darkColorScheme(
    primary = Palette.Blue80,
    onPrimary = Palette.Blue20,
    primaryContainer = Palette.Blue30,
    onPrimaryContainer = Palette.Blue90,
    secondary = Palette.Teal80,
    onSecondary = Palette.Teal10,
    secondaryContainer = Palette.Teal30,
    onSecondaryContainer = Palette.Teal90,
    tertiary = Palette.Amber80,
    onTertiary = Palette.Amber20,
    tertiaryContainer = Palette.Amber30,
    onTertiaryContainer = Palette.Amber90,
    error = Palette.Error80,
    onError = Palette.Error10,
    errorContainer = Palette.Error40,
    onErrorContainer = Palette.Error90,
    background = Palette.Neutral4,
    onBackground = Palette.NeutralVar90,
    surface = Palette.Neutral4,
    onSurface = Palette.NeutralVar90,
    surfaceVariant = Palette.Neutral17,
    onSurfaceVariant = Palette.NeutralVar80,
    surfaceContainer = Palette.Neutral17,
    surfaceContainerHigh = Palette.Neutral22,
    surfaceContainerHighest = Palette.Neutral22,
    surfaceContainerLow = Palette.Neutral12,
    surfaceContainerLowest = Palette.Neutral6,
    outline = Palette.NeutralVar60,
    outlineVariant = Palette.NeutralVar30,
    scrim = Palette.Neutral4
)
