package com.racetimer.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.warforged5.racemultitimer.domain.AppTheme

/**
 * Material You Color Palette System
 * All colors are harmonized and derived from tonal palettes
 */
data class AppColorScheme(
    // Primary tones
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    // Secondary tones
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    // Tertiary tones
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    // Error tones (harmonized with primary)
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    // Surface tones
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    // Outline
    val outline: Color,
    val outlineVariant: Color
)

/**
 * Pre-defined Material You palettes
 * Each is a complete, harmonized color system
 */
object MaterialYouPalettes {

    // Indigo - Default, professional
    val indigoLight = AppColorScheme(
        primary = Color(0xFF5A5D95),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFE1E1FB),
        onPrimaryContainer = Color(0xFF16184B),
        secondary = Color(0xFF5E616F),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFE3E4F4),
        onSecondaryContainer = Color(0xFF1B1D2B),
        tertiary = Color(0xFF755B7A),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFF5E0F8),
        onTertiaryContainer = Color(0xFF2D1A32),
        error = Color(0xFF8C4A60),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFD9DE),
        onErrorContainer = Color(0xFF3B071D),
        surface = Color(0xFFFBF8FF),
        onSurface = Color(0xFF1B1B21),
        surfaceVariant = Color(0xFFE4E1EC),
        onSurfaceVariant = Color(0xFF46464F),
        surfaceContainer = Color(0xFFFFFFFF),
        surfaceContainerHigh = Color(0xFFF3F0F9),
        surfaceContainerHighest = Color(0xFFEDEAF3),
        outline = Color(0xFF777680),
        outlineVariant = Color(0xFFC8C5D0)
    )

    val indigoDark = AppColorScheme(
        primary = Color(0xFFC3C5F3),
        onPrimary = Color(0xFF2C2F60),
        primaryContainer = Color(0xFF43467B),
        onPrimaryContainer = Color(0xFFE1E1FB),
        secondary = Color(0xFFC7C8D8),
        onSecondary = Color(0xFF303240),
        secondaryContainer = Color(0xFF474957),
        onSecondaryContainer = Color(0xFFE3E4F4),
        tertiary = Color(0xFFE2C4E6),
        onTertiary = Color(0xFF432E48),
        tertiaryContainer = Color(0xFF5B4461),
        onTertiaryContainer = Color(0xFFF5E0F8),
        error = Color(0xFFFFB2BE),
        onError = Color(0xFF561D32),
        errorContainer = Color(0xFF723348),
        onErrorContainer = Color(0xFFFFD9DE),
        surface = Color(0xFF131318),
        onSurface = Color(0xFFE4E1E9),
        surfaceVariant = Color(0xFF46464F),
        onSurfaceVariant = Color(0xFFC8C5D0),
        surfaceContainer = Color(0xFF1F1F26),
        surfaceContainerHigh = Color(0xFF2A2A31),
        surfaceContainerHighest = Color(0xFF35353C),
        outline = Color(0xFF918F9A),
        outlineVariant = Color(0xFF46464F)
    )

    // Teal - Fresh, calm
    val tealLight = AppColorScheme(
        primary = Color(0xFF006A69),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFF9EF2F0),
        onPrimaryContainer = Color(0xFF002020),
        secondary = Color(0xFF4A6362),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFCCE8E7),
        onSecondaryContainer = Color(0xFF051F1F),
        tertiary = Color(0xFF4B607C),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFD3E4FF),
        onTertiaryContainer = Color(0xFF041C35),
        error = Color(0xFF8C4A60),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFD9DE),
        onErrorContainer = Color(0xFF3B071D),
        surface = Color(0xFFF4FBFA),
        onSurface = Color(0xFF161D1D),
        surfaceVariant = Color(0xFFDAE5E4),
        onSurfaceVariant = Color(0xFF3F4948),
        surfaceContainer = Color(0xFFFFFFFF),
        surfaceContainerHigh = Color(0xFFEEF5F4),
        surfaceContainerHighest = Color(0xFFE8EFEE),
        outline = Color(0xFF6F7978),
        outlineVariant = Color(0xFFBEC9C8)
    )

    val tealDark = AppColorScheme(
        primary = Color(0xFF82D5D4),
        onPrimary = Color(0xFF003736),
        primaryContainer = Color(0xFF00504F),
        onPrimaryContainer = Color(0xFF9EF2F0),
        secondary = Color(0xFFB0CCCB),
        onSecondary = Color(0xFF1B3534),
        secondaryContainer = Color(0xFF324B4A),
        onSecondaryContainer = Color(0xFFCCE8E7),
        tertiary = Color(0xFFB4C8E8),
        onTertiary = Color(0xFF1D314B),
        tertiaryContainer = Color(0xFF344863),
        onTertiaryContainer = Color(0xFFD3E4FF),
        error = Color(0xFFFFB2BE),
        onError = Color(0xFF561D32),
        errorContainer = Color(0xFF723348),
        onErrorContainer = Color(0xFFFFD9DE),
        surface = Color(0xFF0E1514),
        onSurface = Color(0xFFDEE4E3),
        surfaceVariant = Color(0xFF3F4948),
        onSurfaceVariant = Color(0xFFBEC9C8),
        surfaceContainer = Color(0xFF1A2121),
        surfaceContainerHigh = Color(0xFF252B2B),
        surfaceContainerHighest = Color(0xFF303636),
        outline = Color(0xFF899392),
        outlineVariant = Color(0xFF3F4948)
    )

    // Rose - Warm, energetic
    val roseLight = AppColorScheme(
        primary = Color(0xFF9C4057),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFFD9DE),
        onPrimaryContainer = Color(0xFF3F0017),
        secondary = Color(0xFF75565C),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFFD9DE),
        onSecondaryContainer = Color(0xFF2B151A),
        tertiary = Color(0xFF795831),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFFDDBB),
        onTertiaryContainer = Color(0xFF2A1700),
        error = Color(0xFF8C4A60),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFD9DE),
        onErrorContainer = Color(0xFF3B071D),
        surface = Color(0xFFFFF8F7),
        onSurface = Color(0xFF22191B),
        surfaceVariant = Color(0xFFF4DDE0),
        onSurfaceVariant = Color(0xFF524345),
        surfaceContainer = Color(0xFFFFFFFF),
        surfaceContainerHigh = Color(0xFFFCECEE),
        surfaceContainerHighest = Color(0xFFF6E6E8),
        outline = Color(0xFF847375),
        outlineVariant = Color(0xFFD7C1C4)
    )

    val roseDark = AppColorScheme(
        primary = Color(0xFFFFB1BF),
        onPrimary = Color(0xFF5E112A),
        primaryContainer = Color(0xFF7D2940),
        onPrimaryContainer = Color(0xFFFFD9DE),
        secondary = Color(0xFFE5BDC2),
        onSecondary = Color(0xFF43292E),
        secondaryContainer = Color(0xFF5C3F44),
        onSecondaryContainer = Color(0xFFFFD9DE),
        tertiary = Color(0xFFEBBF91),
        onTertiary = Color(0xFF452B08),
        tertiaryContainer = Color(0xFF5F411D),
        onTertiaryContainer = Color(0xFFFFDDBB),
        error = Color(0xFFFFB2BE),
        onError = Color(0xFF561D32),
        errorContainer = Color(0xFF723348),
        onErrorContainer = Color(0xFFFFD9DE),
        surface = Color(0xFF1A1113),
        onSurface = Color(0xFFF0DEE0),
        surfaceVariant = Color(0xFF524345),
        onSurfaceVariant = Color(0xFFD7C1C4),
        surfaceContainer = Color(0xFF261C1E),
        surfaceContainerHigh = Color(0xFF312628),
        surfaceContainerHighest = Color(0xFF3D3133),
        outline = Color(0xFF9F8C8E),
        outlineVariant = Color(0xFF524345)
    )

    // Sage - Nature, calm
    val sageLight = AppColorScheme(
        primary = Color(0xFF4B6546),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFCDEBC3),
        onPrimaryContainer = Color(0xFF082108),
        secondary = Color(0xFF54634E),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFD7E8CE),
        onSecondaryContainer = Color(0xFF121F0F),
        tertiary = Color(0xFF396569),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFBCEBEF),
        onTertiaryContainer = Color(0xFF002022),
        error = Color(0xFF8C4A60),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFD9DE),
        onErrorContainer = Color(0xFF3B071D),
        surface = Color(0xFFF8FAF2),
        onSurface = Color(0xFF191D17),
        surfaceVariant = Color(0xFFDFE4D8),
        onSurfaceVariant = Color(0xFF43483F),
        surfaceContainer = Color(0xFFFFFFFF),
        surfaceContainerHigh = Color(0xFFF0F4EC),
        surfaceContainerHighest = Color(0xFFEAEEE6),
        outline = Color(0xFF73796E),
        outlineVariant = Color(0xFFC3C8BC)
    )

    val sageDark = AppColorScheme(
        primary = Color(0xFFB1CFA8),
        onPrimary = Color(0xFF1E361A),
        primaryContainer = Color(0xFF344D2F),
        onPrimaryContainer = Color(0xFFCDEBC3),
        secondary = Color(0xFFBBCBB3),
        onSecondary = Color(0xFF263423),
        secondaryContainer = Color(0xFF3C4B38),
        onSecondaryContainer = Color(0xFFD7E8CE),
        tertiary = Color(0xFFA0CFD3),
        onTertiary = Color(0xFF00363A),
        tertiaryContainer = Color(0xFF1F4D51),
        onTertiaryContainer = Color(0xFFBCEBEF),
        error = Color(0xFFFFB2BE),
        onError = Color(0xFF561D32),
        errorContainer = Color(0xFF723348),
        onErrorContainer = Color(0xFFFFD9DE),
        surface = Color(0xFF11140F),
        onSurface = Color(0xFFE1E4DC),
        surfaceVariant = Color(0xFF43483F),
        onSurfaceVariant = Color(0xFFC3C8BC),
        surfaceContainer = Color(0xFF1D201A),
        surfaceContainerHigh = Color(0xFF272B24),
        surfaceContainerHighest = Color(0xFF32362F),
        outline = Color(0xFF8D9287),
        outlineVariant = Color(0xFF43483F)
    )

    // Amber - Warm, energetic, golden
    val amberLight = AppColorScheme(
        primary = Color(0xFF855400),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFFDDB5),
        onPrimaryContainer = Color(0xFF2A1700),
        secondary = Color(0xFF6F5B40),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFBDEBC),
        onSecondaryContainer = Color(0xFF271904),
        tertiary = Color(0xFF51643F),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFD4EABB),
        onTertiaryContainer = Color(0xFF102004),
        error = Color(0xFF8C4A60),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFD9DE),
        onErrorContainer = Color(0xFF3B071D),
        surface = Color(0xFFFFF8F4),
        onSurface = Color(0xFF1F1B16),
        surfaceVariant = Color(0xFFF0E0CF),
        onSurfaceVariant = Color(0xFF4F4539),
        surfaceContainer = Color(0xFFFFFFFF),
        surfaceContainerHigh = Color(0xFFFAF0E6),
        surfaceContainerHighest = Color(0xFFF4EAE0),
        outline = Color(0xFF817567),
        outlineVariant = Color(0xFFD3C4B4)
    )

    val amberDark = AppColorScheme(
        primary = Color(0xFFFFB958),
        onPrimary = Color(0xFF462A00),
        primaryContainer = Color(0xFF653E00),
        onPrimaryContainer = Color(0xFFFFDDB5),
        secondary = Color(0xFFDDC3A2),
        onSecondary = Color(0xFF3E2E16),
        secondaryContainer = Color(0xFF56442A),
        onSecondaryContainer = Color(0xFFFBDEBC),
        tertiary = Color(0xFFB8CEA1),
        onTertiary = Color(0xFF243515),
        tertiaryContainer = Color(0xFF3A4C29),
        onTertiaryContainer = Color(0xFFD4EABB),
        error = Color(0xFFFFB2BE),
        onError = Color(0xFF561D32),
        errorContainer = Color(0xFF723348),
        onErrorContainer = Color(0xFFFFD9DE),
        surface = Color(0xFF17130E),
        onSurface = Color(0xFFEBE1D9),
        surfaceVariant = Color(0xFF4F4539),
        onSurfaceVariant = Color(0xFFD3C4B4),
        surfaceContainer = Color(0xFF231F1A),
        surfaceContainerHigh = Color(0xFF2E2924),
        surfaceContainerHighest = Color(0xFF39342E),
        outline = Color(0xFF9C8E80),
        outlineVariant = Color(0xFF4F4539)
    )

    // Ocean - Cool, deep, calming blue
    val oceanLight = AppColorScheme(
        primary = Color(0xFF00629E),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFCFE5FF),
        onPrimaryContainer = Color(0xFF001D34),
        secondary = Color(0xFF516379),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFD4E7FF),
        onSecondaryContainer = Color(0xFF0C1F31),
        tertiary = Color(0xFF675A78),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFEEDDFF),
        onTertiaryContainer = Color(0xFF221732),
        error = Color(0xFF8C4A60),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFD9DE),
        onErrorContainer = Color(0xFF3B071D),
        surface = Color(0xFFF7F9FF),
        onSurface = Color(0xFF181C22),
        surfaceVariant = Color(0xFFDEE3EB),
        onSurfaceVariant = Color(0xFF42474E),
        surfaceContainer = Color(0xFFFFFFFF),
        surfaceContainerHigh = Color(0xFFEEF3FA),
        surfaceContainerHighest = Color(0xFFE8EDF4),
        outline = Color(0xFF72777F),
        outlineVariant = Color(0xFFC2C7CF)
    )

    val oceanDark = AppColorScheme(
        primary = Color(0xFF99CBFF),
        onPrimary = Color(0xFF003355),
        primaryContainer = Color(0xFF004A79),
        onPrimaryContainer = Color(0xFFCFE5FF),
        secondary = Color(0xFFB8CBE2),
        onSecondary = Color(0xFF233447),
        secondaryContainer = Color(0xFF3A4B5F),
        onSecondaryContainer = Color(0xFFD4E7FF),
        tertiary = Color(0xFFD4BEE6),
        onTertiary = Color(0xFF382C48),
        tertiaryContainer = Color(0xFF4F4260),
        onTertiaryContainer = Color(0xFFEEDDFF),
        error = Color(0xFFFFB2BE),
        onError = Color(0xFF561D32),
        errorContainer = Color(0xFF723348),
        onErrorContainer = Color(0xFFFFD9DE),
        surface = Color(0xFF101419),
        onSurface = Color(0xFFE0E2E9),
        surfaceVariant = Color(0xFF42474E),
        onSurfaceVariant = Color(0xFFC2C7CF),
        surfaceContainer = Color(0xFF1C2026),
        surfaceContainerHigh = Color(0xFF262A31),
        surfaceContainerHighest = Color(0xFF31353C),
        outline = Color(0xFF8C9199),
        outlineVariant = Color(0xFF42474E)
    )
}

/**
 * Extended colors for app-specific needs
 * All derived from the current palette
 */
data class ExtendedColors(
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        success = Color(0xFF4B6546),
        onSuccess = Color(0xFFFFFFFF),
        successContainer = Color(0xFFCDEBC3),
        onSuccessContainer = Color(0xFF082108),
        warning = Color(0xFF795831),
        onWarning = Color(0xFFFFFFFF),
        warningContainer = Color(0xFFFFDDBB),
        onWarningContainer = Color(0xFF2A1700)
    )
}

/**
 * Clean typography
 */
@Composable
fun appTypography() = Typography(
    displayLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 48.sp, lineHeight = 56.sp),
    displayMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 36.sp, lineHeight = 44.sp),
    displaySmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 36.sp),
    headlineLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 32.sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 28.sp),
    headlineSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 18.sp, lineHeight = 24.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 24.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 22.sp),
    titleSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
    labelMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 10.sp, lineHeight = 14.sp)
)

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

fun AppColorScheme.toMaterialColorScheme() = lightColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    secondary = secondary,
    onSecondary = onSecondary,
    secondaryContainer = secondaryContainer,
    onSecondaryContainer = onSecondaryContainer,
    tertiary = tertiary,
    onTertiary = onTertiary,
    tertiaryContainer = tertiaryContainer,
    onTertiaryContainer = onTertiaryContainer,
    error = error,
    onError = onError,
    errorContainer = errorContainer,
    onErrorContainer = onErrorContainer,
    surface = surface,
    onSurface = onSurface,
    surfaceVariant = surfaceVariant,
    onSurfaceVariant = onSurfaceVariant,
    surfaceContainer = surfaceContainer,
    surfaceContainerHigh = surfaceContainerHigh,
    surfaceContainerHighest = surfaceContainerHighest,
    outline = outline,
    outlineVariant = outlineVariant
)

@Composable
fun RaceTimerTheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    palette: String = "indigo", // indigo, teal, rose, sage
    content: @Composable () -> Unit
) {
    val isDark = when (appTheme) {
        AppTheme.SYSTEM -> isSystemInDarkTheme()
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.DYNAMIC -> isSystemInDarkTheme()
    }

    val appColorScheme = when (palette) {
        "teal" -> if (isDark) MaterialYouPalettes.tealDark else MaterialYouPalettes.tealLight
        "rose" -> if (isDark) MaterialYouPalettes.roseDark else MaterialYouPalettes.roseLight
        "sage" -> if (isDark) MaterialYouPalettes.sageDark else MaterialYouPalettes.sageLight
        "amber" -> if (isDark) MaterialYouPalettes.amberDark else MaterialYouPalettes.amberLight
        "ocean" -> if (isDark) MaterialYouPalettes.oceanDark else MaterialYouPalettes.oceanLight
        else -> if (isDark) MaterialYouPalettes.indigoDark else MaterialYouPalettes.indigoLight
    }

    // Success and warning colors harmonized with palette
    val extendedColors = if (isDark) {
        ExtendedColors(
            success = appColorScheme.tertiary,
            onSuccess = appColorScheme.onTertiary,
            successContainer = appColorScheme.tertiaryContainer,
            onSuccessContainer = appColorScheme.onTertiaryContainer,
            warning = Color(0xFFEBBF91),
            onWarning = Color(0xFF452B08),
            warningContainer = Color(0xFF5F411D),
            onWarningContainer = Color(0xFFFFDDBB)
        )
    } else {
        ExtendedColors(
            success = appColorScheme.tertiary,
            onSuccess = appColorScheme.onTertiary,
            successContainer = appColorScheme.tertiaryContainer,
            onSuccessContainer = appColorScheme.onTertiaryContainer,
            warning = Color(0xFF795831),
            onWarning = Color(0xFFFFFFFF),
            warningContainer = Color(0xFFFFDDBB),
            onWarningContainer = Color(0xFF2A1700)
        )
    }

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = appColorScheme.toMaterialColorScheme(),
            shapes = AppShapes,
            typography = appTypography(),
            content = content
        )
    }
}

object AppThemeExtras {
    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}