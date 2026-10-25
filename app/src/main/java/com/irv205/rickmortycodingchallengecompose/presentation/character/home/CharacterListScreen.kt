/**
 * Archivo: CharacterListScreen.kt
 * 
 * Propósito:
 * Pantalla principal que muestra la lista de personajes con paginación infinita.
 * Sigue el patrón State-Action: recibe estado, emite acciones al ViewModel.
 * 
 * Principios:
 * - UI sin lógica de negocio
 * - Sin uso de operadores null-unsafe (!!)
 * - Unidirectional Data Flow
 * - Componentes Composables puros donde es posible
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
 * Solo se encarga de renderizar UI y emitir acciones.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    onNavigateToDetail: (Int) -> Unit,
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CharacterListContent(
        state = uiState,
        onAction = { action ->
            when (action) {
                is CharacterListAction.OnCharacterClicked -> onNavigateToDetail(action.characterId)
                else -> viewModel.onAction(action)
            }
        }
    )
}

/**
 * Composable puramente presentacional para la lista de personajes.
 * No tiene dependencias con ViewModel, facilitando testing y previews.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterListContent(
    state: CharacterListState,
    onAction: (CharacterListAction) -> Unit
) {
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        onAction(CharacterListAction.LoadCharacters)
    }

    // Mostrar error en Snackbar cuando exista
    LaunchedEffect(state.hasError, state.errorMessage) {
        val message = state.errorMessage
        if (state.hasError && !message.isNullOrBlank()) {
            snackbarHostState.showSnackbar(message)
            onAction(CharacterListAction.ClearError)
        }
    }

    // Detectar scroll cerca del final para paginación infinita
    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            Pair(lastVisibleItem, totalItems)
        }
            .distinctUntilChanged()
            .collect { (lastVisible, total) ->
                onAction(
                    CharacterListAction.OnScrollNearEnd(
                        lastVisibleItemIndex = lastVisible,
                        totalItemsCount = total
                    )
                )
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
                state.isLoading && state.characters.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.characters.isNotEmpty() -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = state.characters,
                            key = { character -> character.id }
                        ) { character ->
                            CharacterListItem(
                                character = character,
                                onClick = {
                                    onAction(CharacterListAction.OnCharacterClicked(character.id))
                                }
                            )
                        }

                        if (state.isLoadingMore) {
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

/**
 * Preview con datos de ejemplo usando el patrón State-Action
 */
@Preview(showBackground = true)
@Composable
fun CharacterListScreenPreview() {
    RickMortyCodingChallengeComposeTheme {
        CharacterListContent(
            state = CharacterListState(
                characters = characters,
                isLoading = true,
                isLoadingMore = false,
                hasError = false,
                errorMessage = null
            ),
            onAction = {}
        )
    }
}
