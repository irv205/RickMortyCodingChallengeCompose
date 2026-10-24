/**
 * Archivo: CharacterListContract.kt
 * 
 * Propósito:
 * Define el contrato de UI para la pantalla de lista de personajes siguiendo el patrón
 * State-Action (Unidirectional Data Flow).
 * 
 * Patrón State-Action:
 * - State: Representa el estado inmutable de la UI
 * - Action/Intent: Acciones que el usuario puede realizar o eventos del sistema
 * - El ViewModel procesa las acciones y actualiza el State
 * - La UI es puramente reactiva, solo observa State y emite Actions
 */
package com.irv205.rickmortycodingchallengecompose.presentation.character.home

import com.irv205.rickmortycodingchallengecompose.domain.model.Character

/**
 * Estado inmutable de la UI para la pantalla de lista de personajes.
 * La UI debe leer únicamente este estado para renderizarse.
 */
data class CharacterListState(
    val characters: List<Character> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val hasError: Boolean = false
)

/**
 * Acciones/Intents que pueden ocurrir en la pantalla de lista de personajes.
 * Todas las interacciones del usuario y eventos se representan como acciones.
 */
sealed interface CharacterListAction {
    /** Acción para cargar la primera página de personajes */
    data object LoadCharacters : CharacterListAction
    
    /** Acción para cargar más personajes (paginación infinita) */
    data object LoadMoreCharacters : CharacterListAction
    
    /** Acción cuando el usuario hace clic en un personaje */
    data class OnCharacterClicked(val characterId: Int) : CharacterListAction
    
    /** Acción para limpiar el error mostrado */
    data object ClearError : CharacterListAction
    
    /** Acción cuando el scroll alcanza cerca del final de la lista */
    data class OnScrollNearEnd(val lastVisibleItemIndex: Int, val totalItemsCount: Int) : CharacterListAction
}
