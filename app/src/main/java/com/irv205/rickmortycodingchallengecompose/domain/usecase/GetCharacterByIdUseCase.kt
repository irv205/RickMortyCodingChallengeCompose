/**
 * Archivo: GetCharacterByIdUseCase.kt
 * 
 * Propósito:
 * Use Case para obtener el detalle de un personaje específico por su ID.
 * Usado en la pantalla de detalle cuando el usuario selecciona un personaje.
 */
package com.irv205.rickmortycodingchallengecompose.domain.usecase

import com.irv205.rickmortycodingchallengecompose.core.util.Resource
import com.irv205.rickmortycodingchallengecompose.domain.model.Character
import com.irv205.rickmortycodingchallengecompose.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacterByIdUseCase @Inject constructor(
    private val repository: AppRepository
) {
    /**
     * Ejecuta el caso de uso para obtener detalle de personaje.
     * @param id ID único del personaje
     * @return Flow con estados Resource<Character>
     */
    suspend operator fun invoke(id: Int): Flow<Resource<Character>> = repository.getCharacterById(id)
}
