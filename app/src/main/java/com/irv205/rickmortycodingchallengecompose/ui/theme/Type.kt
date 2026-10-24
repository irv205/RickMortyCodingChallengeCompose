/**
 * Archivo: Type.kt
 * 
 * Propósito:
 * Define la tipografía (fuentes, tamaños, pesos) para la aplicación.
 * 
 * Material Design 3 Typography:
 * Usa un sistema tipográfico con diferentes estilos (display, headline, title, body, label)
 * que se adaptan a diferentes jerarquías visuales.
 * 
 * Por defecto usa las fuentes del sistema, pero se pueden personalizar
 * con FontFamily personalizado si se desea.
 */
package com.irv205.rickmortycodingchallengecompose.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Conjunto de estilos tipográficos de Material Design 3
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* 
     * Se pueden añadir más estilos si se necesitan:
     * titleLarge, headlineMedium, labelSmall, etc.
     * MaterialTheme.typography.titleMedium, MaterialTheme.typography.bodySmall, etc.
     */
)
