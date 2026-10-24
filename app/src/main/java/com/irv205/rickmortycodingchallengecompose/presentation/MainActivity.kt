/**
 * Archivo: MainActivity.kt
 * 
 * Propósito:
 * Activity principal de la aplicación. Es el único Activity en esta app (Single Activity Architecture).
 * Aquí se configura el tema, se habilita edge-to-edge y se monta la UI con Jetpack Compose.
 * 
 * Responsabilidades:
 * - Configurar tema de la aplicación
 * - Habilitar modo edge-to-edge
 * - Inicializar el grafo de navegación
 * 
 * La navegación ha sido separada a [AppNavGraph] siguiendo el principio de responsabilidad única.
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
import androidx.navigation.compose.rememberNavController
import com.irv205.rickmortycodingchallengecompose.presentation.navigation.AppNavGraph
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
                    RickMortyApp()
                }
            }
        }
    }
}

/**
 * Composable raíz de la aplicación.
 * Se encarga únicamente de crear el NavController y delegar la navegación al AppNavGraph.
 */
@Composable
fun RickMortyApp() {
    val navController = rememberNavController()
    AppNavGraph(navController = navController)
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
