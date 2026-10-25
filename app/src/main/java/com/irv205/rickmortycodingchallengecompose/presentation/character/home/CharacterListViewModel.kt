/**
 * Archivo: CharacterListViewModel.kt
 * 
 * Propósito:
 * ViewModel para la pantalla de lista de personajes siguiendo el patrón State-Action.
 * Gestiona el estado UI, lógica de paginación infinita y comunicación con UseCases.
 * 
 * Principios aplicados:
 * - Unidirectional Data Flow (UDF): La UI emite acciones, el ViewModel actualiza el estado
 * - Estado inmutable expuesto a través de StateFlow
 * - Toda la lógica de negocio se mantiene en el ViewModel, fuera de la UI
 * - Sin null unsafe operators (!!)
 */
package com.irv205.rickmortycodingchallengecompose.presentation.character.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irv205.rickmortycodingchallengecompose.core.util.Resource
import com.irv205.rickmortycodingchallengecompose.domain.usecase.GetCharactersByUrlUseCase
import com.irv205.rickmortycodingchallengecompose.domain.usecase.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para gestionar lógica y estado de la lista de personajes.
 * Implementa el patrón State-Action para un flujo de datos unidireccional.
 */
@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getCharactersByUrlUseCase: GetCharactersByUrlUseCase
) : ViewModel() {

    // Estado mutable privado - única fuente de verdad
    private val _uiState = MutableStateFlow(CharacterListState())
    // Estado inmutable expuesto a la UI
    val uiState: StateFlow<CharacterListState> = _uiState.asStateFlow()

    // URL de la siguiente página para paginación
    private var nextPageUrl: String? = null

    /**
     * Punto único de entrada para todas las acciones de la UI.
     * Procesa cada acción y actualiza el estado correspondiente.
     */
    fun onAction(action: CharacterListAction) {
        when (action) {
            is CharacterListAction.LoadCharacters -> loadCharacters()
            is CharacterListAction.LoadMoreCharacters -> loadMore()
            is CharacterListAction.OnCharacterClicked -> {
                // La navegación se delega a la UI/NavGraph a través de callbacks
            }
            is CharacterListAction.ClearError -> clearError()
            is CharacterListAction.OnScrollNearEnd -> handleScrollNearEnd(action)
        }
    }

    /**
     * Carga la primera página de personajes.
     */
    private fun loadCharacters() {
        viewModelScope.launch {
            getCharactersUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = currentState.characters.isEmpty(),
                                hasError = false,
                                errorMessage = null
                            )
                        }
                    }

                    is Resource.Success -> {
                        nextPageUrl = resource.data.nextPageUrl
                        val newCharacters = resource.data.characters
                        val currentCharacters = _uiState.value.characters
                        val mergedCharacters = (currentCharacters + newCharacters).distinctBy { it.id }

                        _uiState.update { currentState ->
                            currentState.copy(
                                characters = mergedCharacters,
                                isLoading = false,
                                isLoadingMore = false,
                                hasError = false,
                                errorMessage = null
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                isLoadingMore = false,
                                hasError = true,
                                errorMessage = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Carga la siguiente página si existe y no hay una carga en progreso.
     */
    private fun loadMore() {
        val url = nextPageUrl
        if (url.isNullOrBlank() || _uiState.value.isLoading || _uiState.value.isLoadingMore) {
            return
        }

        viewModelScope.launch {
            getCharactersByUrlUseCase(url).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoadingMore = true,
                                hasError = false,
                                errorMessage = null
                            )
                        }
                    }

                    is Resource.Success -> {
                        nextPageUrl = resource.data.nextPageUrl
                        val newCharacters = resource.data.characters
                        val currentCharacters = _uiState.value.characters
                        val mergedCharacters = (currentCharacters + newCharacters).distinctBy { it.id }

                        _uiState.update { currentState ->
                            currentState.copy(
                                characters = mergedCharacters,
                                isLoadingMore = false,
                                hasError = false,
                                errorMessage = null
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoadingMore = false,
                                hasError = true,
                                errorMessage = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Maneja la detección de scroll cerca del final para activar paginación infinita.
     */
    private fun handleScrollNearEnd(action: CharacterListAction.OnScrollNearEnd) {
        if (action.totalItemsCount > 0 && action.lastVisibleItemIndex >= action.totalItemsCount - 3) {
            loadMore()
        }
    }

    /**
     * Limpia el mensaje de error del estado.
     */
    private fun clearError() {
        _uiState.update { currentState ->
            currentState.copy(
                hasError = false,
                errorMessage = null
            )
        }
    }
}
