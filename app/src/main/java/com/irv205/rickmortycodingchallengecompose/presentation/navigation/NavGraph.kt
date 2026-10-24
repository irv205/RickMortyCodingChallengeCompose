/**
 * Archivo: NavGraph.kt
 * 
 * Propósito:
 * Grafo de navegación separado de MainActivity para mantener la responsabilidad única.
 * Aquí se definen todas las rutas, transiciones y argumentos de navegación.
 * 
 * Arquitectura: Single Activity + Navigation Compose
 * - MainActivity solo se encarga de configurar el tema y el contenedor de la app
 * - NavGraph se encarga exclusivamente de la lógica de navegación
 */
package com.irv205.rickmortycodingchallengecompose.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.irv205.rickmortycodingchallengecompose.presentation.character.detail.CharacterDetailScreen
import com.irv205.rickmortycodingchallengecompose.presentation.character.home.CharacterListScreen

/**
 * Composable que define el grafo de navegación completo de la aplicación.
 * 
 * @param navController Controlador de navegación para gestionar el back stack
 */
@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.CharacterList
    ) {
        // Ruta: Lista de personajes
        composable<Screen.CharacterList> {
            CharacterListScreen(
                onNavigateToDetail = { characterId ->
                    navController.navigate(Screen.CharacterDetail(characterId = characterId))
                }
            )
        }

        // Ruta: Detalle de personaje
        composable<Screen.CharacterDetail> { backStackEntry ->
            val characterDetailRoute = backStackEntry.toRoute<Screen.CharacterDetail>()
            CharacterDetailScreen(
                characterId = characterDetailRoute.characterId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
