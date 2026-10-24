/**
 * Archivo: Theme.kt
 * 
 * Propósito:
 * Configura el tema de la aplicación con soporte para modo claro/oscuro y colores dinámicos.
 * 
 * Características:
 * - Soporte modo claro/oscuro automático según configuración del sistema
 * - Dynamic Color (Material You) en Android 12+ (API 31+)
 * - Usa colorScheme, typography de Material Design 3
 * - Incluye Previews para visualizar ambos temas
 * 
 * Dynamic Color:
 * En Android 12+, puede usar los colores del wallpaper del usuario.
 * En versiones anteriores o si está deshabilitado, usa los colores definidos manualmente.
 * 
 * @Composable theme wrapper: Debe envolver toda la UI para que MaterialTheme
 * proporcione colores, tipografía y formas a todos los Composables hijos.
 */
package com.irv205.rickmortycodingchallengecompose.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

// Esquema de colores para modo oscuro
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// Esquema de colores para modo claro
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Otros colores por defecto que se pueden sobrescribir:
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

/**
 * Tema principal de la aplicación.
 * 
 * @param darkTheme Si true usa modo oscuro, si false modo claro.
 *                  Por defecto detecta configuración del sistema.
 * @param dynamicColor Si true usa colores dinámicos en Android 12+.
 *                    Por defecto habilitado.
 * @param content Contenido Composable al que se aplica el tema
 */
@Composable
fun RickMortyCodingChallengeComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color está disponible en Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Usar colores dinámicos si está disponible (Android 12+)
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Usar esquema oscuro manual
        darkTheme -> DarkColorScheme
        // Usar esquema claro manual
        else -> LightColorScheme
    }

    // Aplica el tema a todo el contenido
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * Preview del tema en modo claro
 */
@Preview(showBackground = true)
@Composable
fun RickMortyCodingChallengeComposeThemeLightPreview() {
    RickMortyCodingChallengeComposeTheme(darkTheme = false) {
        androidx.compose.material3.Surface {
            androidx.compose.material3.Text("Light Theme")
        }
    }
}

/**
 * Preview del tema en modo oscuro
 */
@Preview(showBackground = true)
@Composable
fun RickMortyCodingChallengeComposeThemeDarkPreview() {
    RickMortyCodingChallengeComposeTheme(darkTheme = true) {
        androidx.compose.material3.Surface {
            androidx.compose.material3.Text("Dark Theme")
        }
    }
}
