/**
 * Archivo: MainActivity.kt
 * 
 * Propósito:
 * Activity principal de la aplicación. Es el único Activity en esta app (Single Activity Architecture).
 * Aquí se configura el tema, se habilita edge-to-edge y se monta la UI con Jetpack Compose.
 * 
 * Arquitectura: Single Activity + Navigation Compose
 * - En lugar de múltiples Activities, usamos una sola Activity y navegamos entre Composables
 *   mediante Navigation Compose. Esto simplifica el ciclo de vida y la gestión del estado.
 * 
 * Conceptos clave:
 * - @AndroidEntryPoint: Permite a Hilt inyectar ViewModels y dependencias en esta Activity.
 * - enableEdgeToEdge(): Permite que el contenido se dibuje detrás de las barras del sistema (status bar, navigation bar).
 * - setContent { }: Función de Jetpack Compose para establecer la UI declarativa.
 * - NavHost + rememberNavController: Sistema de navegación type-safe con Kotlin Serialization.
 * - toRoute<T>(): Extrae los argumentos de navegación de forma segura con rutas serializables.
 */
package com.irv205.rickmortycodingchallengecompose.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.irv205.rickmortycodingchallengecompose.presentation.character.detail.CharacterDetailScreen
import com.irv205.rickmortycodingchallengecompose.presentation.character.home.CharacterListScreen
import com.irv205.rickmortycodingchallengecompose.presentation.navigation.Screen
import com.irv205.rickmortycodingchallengecompose.ui.theme.RickMortyCodingChallengeComposeTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity principal anotada con @AndroidEntryPoint para habilitar inyección con Hilt.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilita modo edge-to-edge: contenido se extiende bajo barras del sistema
        enableEdgeToEdge()
        // Establece el contenido UI con Jetpack Compose
        setContent {
            // Aplica el tema personalizado de la app (soporta modo claro/oscuro y colores dinámicos)
            RickMortyCodingChallengeComposeTheme {
                // Surface es el contenedor base que aplica color de fondo según el tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Punto de entrada a la navegación de la app
                    RickMortyApp()
                }
            }
        }
    }
}

/**
 * Composable raíz que configura la navegación de la aplicación.
 * 
 * Usa Navigation Compose con rutas type-safe (definidas con @Serializable).
 * - startDestination: Pantalla inicial al abrir la app.
 * - NavHost: Contenedor que muestra el Composable correspondiente a la ruta actual.
 * 
 * Navegación:
 * - CharacterList -> CharacterDetail: Pasa characterId como argumento de ruta
 * - CharacterDetail -> back: Regresa a la pantalla anterior con popBackStack()
 */
@Composable
fun RickMortyApp() {
    // Controlador de navegación - recuerda su estado entre recomposiciones
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.CharacterList
    ) {
        // Ruta para la lista de personajes
        composable<Screen.CharacterList> {
            CharacterListScreen(
                onCharacterClick = { characterId ->
                    // Navega a la pantalla de detalle pasando el ID del personaje
                    navController.navigate(Screen.CharacterDetail(characterId))
                }
            )
        }
        
        // Ruta para el detalle de un personaje
        composable<Screen.CharacterDetail> { backStackEntry ->
            // Extrae los argumentos de la ruta de forma type-safe
            val detail = backStackEntry.toRoute<Screen.CharacterDetail>()
            CharacterDetailScreen(
                characterId = detail.characterId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Preview para visualizar RickMortyApp en Android Studio sin ejecutar la app.
 */
@Preview(showBackground = true)
@Composable
fun RickMortyAppPreview() {
    RickMortyCodingChallengeComposeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RickMortyApp()
        }
    }
}
