package pl.nubet.studymood.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val lightScheme: ColorScheme =
    lightColorScheme(
        primary = GenZColors.Amber500,
        onPrimary = Color.White,
        primaryContainer = GenZColors.Amber100,
        onPrimaryContainer = GenZColors.Amber800,
        secondary = GenZColors.Coffee500,
        onSecondary = Color.White,
        secondaryContainer = GenZColors.Coffee100,
        onSecondaryContainer = GenZColors.Coffee700,
        tertiary = GenZColors.Olive500,
        onTertiary = Color.White,
        tertiaryContainer = GenZColors.Olive100,
        onTertiaryContainer = GenZColors.Olive700,
        background = GenZColors.Greige50,
        onBackground = GenZColors.Greige900,
        surface = GenZColors.Greige100,
        onSurface = GenZColors.Greige900,
        surfaceVariant = GenZColors.Greige200,
        onSurfaceVariant = GenZColors.Greige700,
        outline = GenZColors.Greige300,
        error = GenZColors.Error,
    )

val darkScheme: ColorScheme =
    darkColorScheme(
        primary = GenZColors.Amber400,
        onPrimary = GenZColors.Coffee900,
        primaryContainer = GenZColors.Amber800,
        onPrimaryContainer = GenZColors.Amber100,
        secondary = GenZColors.Coffee300,
        onSecondary = GenZColors.Coffee900,
        secondaryContainer = GenZColors.Coffee500,
        onSecondaryContainer = GenZColors.Coffee100,
        tertiary = GenZColors.Olive300,
        onTertiary = GenZColors.Olive900,
        tertiaryContainer = GenZColors.Olive700,
        onTertiaryContainer = GenZColors.Olive100,
        background = GenZColors.Coffee900,
        onBackground = GenZColors.Greige100,
        surface = GenZColors.Coffee500,
        onSurface = GenZColors.Greige100,
        surfaceVariant = GenZColors.Coffee700,
        onSurfaceVariant = GenZColors.Greige200,
        outline = GenZColors.Greige500,
        outlineVariant = GenZColors.Coffee500,
        error = Color(0xFFFF6B6B),
    )
