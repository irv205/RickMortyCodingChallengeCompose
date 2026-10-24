/**
 * Archivo: CharacterDetailScreen.kt
 * 
 * Propósito:
 * Pantalla de detalle que muestra información completa de un personaje seleccionado.
 * Sigue el patrón State-Action: sin lógica de negocio en la UI, sin operadores null-unsafe.
 */
package com.irv205.rickmortycodingchallengecompose.presentation.character.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.irv205.rickmortycodingchallengecompose.domain.model.Character
import com.irv205.rickmortycodingchallengecompose.ui.theme.RickMortyCodingChallengeComposeTheme

/**
 * Pantalla de detalle de personaje.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    characterId: Int,
    onNavigateBack: () -> Unit,
    viewModel: CharacterDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(characterId) {
        viewModel.onAction(CharacterDetailAction.LoadCharacter(characterId))
    }

    CharacterDetailContent(
        state = uiState,
        onAction = { action ->
            when (action) {
                is CharacterDetailAction.NavigateBack -> onNavigateBack()
                else -> viewModel.onAction(action)
            }
        }
    )
}

/**
 * Composable puramente presentacional para el detalle de personaje.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterDetailContent(
    state: CharacterDetailState,
    onAction: (CharacterDetailAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Character Detail") },
                navigationIcon = {
                    IconButton(onClick = { onAction(CharacterDetailAction.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.hasError && !state.errorMessage.isNullOrBlank() -> {
                    Text(
                        text = state.errorMessage,
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                state.character != null -> {
                    val character = state.character
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AsyncImage(
                            model = character.image,
                            contentDescription = character.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = character.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                DetailRow(label = "Status", value = character.status)
                                DetailRow(label = "Species", value = character.species)
                                DetailRow(
                                    label = "Type",
                                    value = if (character.type.isBlank()) "N/A" else character.type
                                )
                                DetailRow(label = "Gender", value = character.gender)
                                DetailRow(label = "Origin", value = character.originName)
                                DetailRow(label = "Location", value = character.locationName)
                                DetailRow(label = "Created", value = character.created)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable auxiliar para mostrar una fila con etiqueta y valor.
 * 
 * @param label Etiqueta descriptiva
 * @param value Valor a mostrar
 */
@Composable
fun DetailRow(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Preview en estado de carga
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Loading")
@Composable
fun CharacterDetailScreenLoadingPreview() {
    RickMortyCodingChallengeComposeTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Character Detail") },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
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

/**
 * Preview de DetailRow
 */
@Preview(showBackground = true)
@Composable
fun DetailRowPreview() {
    RickMortyCodingChallengeComposeTheme {
        DetailRow(label = "Status", value = "Alive")
    }
}
