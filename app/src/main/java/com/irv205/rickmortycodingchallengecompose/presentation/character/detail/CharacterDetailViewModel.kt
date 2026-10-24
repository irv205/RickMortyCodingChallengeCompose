/**
 * Archivo: CharacterDetailViewModel.kt
 * 
 * Propósito:
 * ViewModel para la pantalla de detalle de personaje siguiendo el patrón State-Action.
 * Gestiona la carga y estado de un personaje específico.
 * 
 * Principios aplicados:
 * - Unidirectional Data Flow (UDF)
 * - Estado inmutable
 * - Sin operadores null-unsafe
 * - Lógica centralizada en ViewModel
 */
package com.irv205.rickmortycodingchallengecompose.presentation.character.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irv205.rickmortycodingchallengecompose.core.util.Resource
import com.irv205.rickmortycodingchallengecompose.domain.usecase.GetCharacterByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para gestionar detalle de personaje con patrón State-Action.
 */
@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterDetailState())
    val uiState: StateFlow<CharacterDetailState> = _uiState.asStateFlow()

    /**
     * Punto único de entrada para todas las acciones de la UI.
     */
    fun onAction(action: CharacterDetailAction) {
        when (action) {
            is CharacterDetailAction.LoadCharacter -> loadCharacter(action.characterId)
            is CharacterDetailAction.NavigateBack -> {
                // La navegación se delega al NavGraph/UI
            }
            is CharacterDetailAction.ClearError -> clearError()
        }
    }

    /**
     * Carga los detalles de un personaje por ID.
     */
    private fun loadCharacter(characterId: Int) {
        viewModelScope.launch {
            getCharacterByIdUseCase(characterId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = true,
                                hasError = false,
                                errorMessage = null
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                character = resource.data,
                                isLoading = false,
                                hasError = false,
                                errorMessage = null
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
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
