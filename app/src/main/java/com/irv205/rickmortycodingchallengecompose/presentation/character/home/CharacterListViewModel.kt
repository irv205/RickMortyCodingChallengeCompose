/**
 * Archivo: CharacterListViewModel.kt
 * 
 * Propósito:
 * ViewModel para la pantalla de lista de personajes.
 * Gestiona el estado UI, lógica de paginación infinita y comunicación con UseCases.
 * 
 * Arquitectura MVVM (Model-View-ViewModel):
 * - Model: Domain (UseCases/Repository) + Data
 * - View: Composables (CharacterListScreen)
 * - ViewModel: Puente entre View y Model, sobrevive a cambios de configuración
 * 
 * Conceptos clave:
 * - @HiltViewModel: Permite a Hilt inyectar dependencias en el ViewModel
 * - StateFlow: Flujo de estado observable, mantiene el último valor (hot flow)
 * - MutableStateFlow: Versión mutable privada, expuesta como StateFlow inmutable
 * - viewModelScope: Coroutine scope ligado al ciclo de vida del ViewModel (se cancela automáticamente)
 * - collect: Recolecta valores del Flow del UseCase
 * 
 * Paginación infinita:
 * - loadCharacters(): Carga primera página
 * - loadMore(): Carga siguiente página usando nextPageUrl
 * - onListScrolled(): Detecta cuándo acercarse al final para cargar más
 * - distinctBy { it.id }: Evita duplicados al fusionar listas
 */
package com.irv205.rickmortycodingchallengecompose.presentation.character.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irv205.rickmortycodingchallengecompose.core.util.Resource
import com.irv205.rickmortycodingchallengecompose.domain.model.Character
import com.irv205.rickmortycodingchallengecompose.domain.usecase.GetCharactersByUrlUseCase
import com.irv205.rickmortycodingchallengecompose.domain.usecase.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Estado UI para la pantalla de lista de personajes.
 * Representa toda la información que la UI necesita para renderizarse.
 * 
 * @param characters Lista acumulada de personajes (incluye todas las páginas cargadas)
 * @param isLoading true durante carga inicial (solo si lista está vacía)
 * @param isLoadingMore true durante carga de página adicional
 * @param error Mensaje de error si falla la operación
 * @param nextPageUrl URL para cargar siguiente página, null si no hay más
 */
data class CharacterListUiState(
    val characters: List<Character> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val nextPageUrl: String? = null
)

/**
 * ViewModel para gestionar lógica y estado de la lista de personajes.
 */
@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getCharactersByUrlUseCase: GetCharactersByUrlUseCase
) : ViewModel() {

    // Estado mutable privado
    private val _uiState = MutableStateFlow(CharacterListUiState())
    // Estado expuesto inmutable para la UI
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    init {
        // Carga automática al crear el ViewModel
        loadCharacters()
    }

    /**
     * Carga la primera página de personajes.
     * Se ejecuta en viewModelScope para cancelar automáticamente si ViewModel se destruye.
     */
    fun loadCharacters() {
        viewModelScope.launch {
            getCharactersUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Solo muestra loading principal si no hay personajes ya cargados
                        _uiState.value = _uiState.value.copy(
                            isLoading = _uiState.value.characters.isEmpty(),
                            error = null
                        )
                    }
                    is Resource.Success -> {
                        val newCharacters = resource.data.characters
                        val currentList = _uiState.value.characters
                        // Fusiona listas evitando duplicados por ID
                        val merged = (currentList + newCharacters).distinctBy { it.id }
                        _uiState.value = _uiState.value.copy(
                            characters = merged,
                            isLoading = false,
                            isLoadingMore = false,
                            error = null,
                            nextPageUrl = resource.data.nextPageUrl
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            error = resource.message
                        )
                    }
                }
            }
        }
    }

    /**
     * Carga la siguiente página si existe.
     * Previene múltiples llamadas simultáneas con validaciones.
     */
    fun loadMore() {
        val nextUrl = _uiState.value.nextPageUrl
        // No cargar si: no hay URL, ya cargando, o cargando más
        if (nextUrl.isNullOrEmpty() || _uiState.value.isLoading || _uiState.value.isLoadingMore) {
            return
        }
        viewModelScope.launch {
            getCharactersByUrlUseCase(nextUrl).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoadingMore = true, error = null)
                    }
                    is Resource.Success -> {
                        val newCharacters = resource.data.characters
                        val currentList = _uiState.value.characters
                        val merged = (currentList + newCharacters).distinctBy { it.id }
                        _uiState.value = _uiState.value.copy(
                            characters = merged,
                            isLoadingMore = false,
                            error = null,
                            nextPageUrl = resource.data.nextPageUrl
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoadingMore = false,
                            error = resource.message
                        )
                    }
                }
            }
        }
    }

    /**
     * Callback para detectar scroll y cargar más automáticamente.
     * Se activa cuando el usuario está cerca del final de la lista.
     * 
     * @param lastVisibleItemIndex Índice del último elemento visible
     * @param totalItemsCount Total de elementos en la lista
     */
    fun onListScrolled(lastVisibleItemIndex: Int, totalItemsCount: Int) {
        // Carga más cuando quedan 3 elementos o menos por mostrar
        if (totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - 3) {
            loadMore()
        }
    }
}
