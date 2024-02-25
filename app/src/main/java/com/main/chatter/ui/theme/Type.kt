package com.main.chatter.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily

// Set of Material typography styles to start with
private val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = FontFamily.SansSerif),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = FontFamily.SansSerif),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = FontFamily.SansSerif),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = FontFamily.SansSerif),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = FontFamily.SansSerif),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = FontFamily.SansSerif),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = FontFamily.SansSerif),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = FontFamily.SansSerif),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = FontFamily.SansSerif),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = FontFamily.SansSerif),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = FontFamily.SansSerif),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = FontFamily.SansSerif),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = FontFamily.SansSerif),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = FontFamily.SansSerif),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = FontFamily.SansSerif)
)