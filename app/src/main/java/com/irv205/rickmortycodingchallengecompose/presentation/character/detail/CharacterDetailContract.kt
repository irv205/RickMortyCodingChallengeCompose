/**
 * Archivo: CharacterDetailContract.kt
 * 
 * Propósito:
 * Define el contrato de UI para la pantalla de detalle siguiendo el patrón State-Action.
 */
package com.irv205.rickmortycodingchallengecompose.presentation.character.detail

import com.irv205.rickmortycodingchallengecompose.domain.model.Character

/**
 * Estado inmutable de la UI para la pantalla de detalle de personaje.
 */
data class CharacterDetailState(
    val character: Character? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val hasError: Boolean = false
)

/**
 * Acciones/Intents para la pantalla de detalle de personaje.
 */
sealed interface CharacterDetailAction {
    /** Acción para cargar los detalles de un personaje por su ID */
    data class LoadCharacter(val characterId: Int) : CharacterDetailAction
    
    /** Acción para volver a la pantalla anterior */
    data object NavigateBack : CharacterDetailAction
    
    /** Acción para limpiar el error mostrado */
    data object ClearError : CharacterDetailAction
}
