/**
 * Archivo: CharacterListScreen.kt
 * 
 * Propósito:
 * Pantalla principal que muestra la lista de personajes con paginación infinita.
 * 
 * Componentes clave:
 * - Scaffold: Estructura base con TopAppBar, contenido, SnackbarHost
 * - LazyColumn: Lista eficiente que solo renderiza items visibles (virtualización)
 * - rememberLazyListState: Controla el estado de scroll de LazyColumn
 * - snapshotFlow: Observa cambios en el estado de scroll
 * - collectAsStateWithLifecycle: Recolecta StateFlow respetando ciclo de vida (evita leaks)
 * - LaunchedEffect: Ejecuta código cuando cambian keys (error, listState)
 * - SnackbarHost: Muestra mensajes de error al usuario
 * 
 * Paginación infinita:
 * Se detecta cuándo el usuario llega cerca del final usando snapshotFlow sobre layoutInfo.
 * Cuando lastVisible >= totalItems - 3, se dispara loadMore() automáticamente.
 */
package com.irv205.rickmortycodingchallengecompose.presentation.character.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.irv205.rickmortycodingchallengecompose.domain.model.Character
import com.irv205.rickmortycodingchallengecompose.ui.theme.RickMortyCodingChallengeComposeTheme
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Pantalla de lista de personajes.
 * 
 * @param onCharacterClick Callback cuando se selecciona un personaje, recibe characterId
 * @param viewModel ViewModel inyectado por Hilt (con hiltViewModel())
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    // Recolecta estado UI respetando ciclo de vida
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Estado para controlar scroll de LazyColumn
    val listState = rememberLazyListState()
    
    // Estado para mostrar Snackbars
    val snackbarHostState = remember { SnackbarHostState() }

    // Muestra error en Snackbar cuando cambia
    LaunchedEffect(uiState.error) {
        uiState.error?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Detecta scroll para paginación infinita
    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            Pair(lastVisibleItem, totalItems)
        }
            // Solo emite cuando hay cambio real
            .distinctUntilChanged()
            .collect { (lastVisible, total) ->
                if (total > 0 && lastVisible >= total - 3) {
                    viewModel.loadMore()
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rick & Morty") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                // Mostrar loading solo en carga inicial sin datos
                uiState.isLoading && uiState.characters.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Items de personajes con key para mejor rendimiento
                        items(
                            items = uiState.characters,
                            key = { it.id }
                        ) { character ->
                            CharacterListItem(
                                character = character,
                                onClick = { onCharacterClick(character.id) }
                            )
                        }
                        // Indicador de carga al hacer scroll infinito
                        if (uiState.isLoadingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Preview con datos de ejemplo
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CharacterListScreenPreview() {
    RickMortyCodingChallengeComposeTheme {
        val characters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                originName = "Earth (C-137)",
                locationName = "Citadel of Ricks",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                created = "2017-11-04T18:48:46.250Z"
            ),
            Character(
                id = 2,
                name = "Morty Smith",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                originName = "unknown",
                locationName = "Citadel of Ricks",
                image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                created = "2017-11-04T18:50:21.651Z"
            )
        )
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Rick & Morty") }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        items = characters,
                        key = { it.id }
                    ) { character ->
                        CharacterListItem(
                            character = character,
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}

/**
 * Preview en estado de carga
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Loading")
@Composable
fun CharacterListScreenLoadingPreview() {
    RickMortyCodingChallengeComposeTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Rick & Morty") }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
