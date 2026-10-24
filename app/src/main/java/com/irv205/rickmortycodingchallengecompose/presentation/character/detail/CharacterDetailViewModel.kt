/**
 * Archivo: CharacterDetailViewModel.kt
 * 
 * Propósito:
 * ViewModel para la pantalla de detalle de personaje.
 * Gestiona la carga y estado de un personaje específico.
 * 
 * Diferencia con CharacterListViewModel:
 * - Solo maneja UN personaje (no lista/paginación)
 * - Carga bajo demanda con loadCharacter(id) cuando llega el ID desde navegación
 * - Estado más simple: character, isLoading, error
 */
package com.irv205.rickmortycodingchallengecompose.presentation.character.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irv205.rickmortycodingchallengecompose.core.util.Resource
import com.irv205.rickmortycodingchallengecompose.domain.model.Character
import com.irv205.rickmortycodingchallengecompose.domain.usecase.GetCharacterByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Estado UI para la pantalla de detalle.
 * 
 * @param character Datos del personaje, null mientras carga o si hay error
 * @param isLoading Indicador de carga
 * @param error Mensaje de error si falla la operación
 */
data class CharacterDetailUiState(
    val character: Character? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel para gestionar detalle de personaje.
 */
@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterDetailUiState())
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    /**
     * Carga los detalles de un personaje por ID.
     * Llamado desde CharacterDetailScreen con LaunchedEffect.
     * 
     * @param id ID del personaje a cargar
     */
    fun loadCharacter(id: Int) {
        viewModelScope.launch {
            getCharacterByIdUseCase(id).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            character = resource.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = resource.message
                        )
                    }
                }
            }
        }
    }
}
