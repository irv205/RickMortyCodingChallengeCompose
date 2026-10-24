/**
 * Archivo: GetCharactersUseCase.kt
 * 
 * Propósito:
 * Use Case (Caso de Uso) para obtener la primera página de personajes.
 * 
 * Clean Architecture - Domain Layer:
 * - Encapsula una regla de negocio específica
 * - Actúa como intermediario entre ViewModel y Repository
 * - Puede agregar lógica adicional (validaciones, transformaciones, combinaciones de repositorios)
 * - En este caso es simple (passthrough), pero es buena práctica mantener la arquitectura
 * 
 * operator fun invoke(): Permite llamar al UseCase como si fuera una función: getCharactersUseCase()
 * 
 * @Inject constructor(): Hilt inyecta el repositorio
 */
package com.irv205.rickmortycodingchallengecompose.domain.usecase

import com.irv205.rickmortycodingchallengecompose.core.util.Resource
import com.irv205.rickmortycodingchallengecompose.domain.model.CharactersPage
import com.irv205.rickmortycodingchallengecompose.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: AppRepository
) {
    /**
     * Ejecuta el caso de uso para obtener primera página de personajes.
     * @return Flow con estados Resource<CharactersPage>
     */
    suspend operator fun invoke(): Flow<Resource<CharactersPage>> = repository.getCharacters()
}
