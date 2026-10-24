/**
 * Archivo: GetCharactersByUrlUseCase.kt
 * 
 * Propósito:
 * Use Case para obtener personajes desde una URL de paginación.
 * Usado por la funcionalidad "infinite scroll" (carga automática al llegar al final).
 * 
 * Recibe la URL completa "next" proporcionada por la API en InfoDTO.
 */
package com.irv205.rickmortycodingchallengecompose.domain.usecase

import com.irv205.rickmortycodingchallengecompose.core.util.Resource
import com.irv205.rickmortycodingchallengecompose.domain.model.CharactersPage
import com.irv205.rickmortycodingchallengecompose.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersByUrlUseCase @Inject constructor(
    private val repository: AppRepository
) {
    /**
     * Ejecuta el caso de uso para cargar siguiente página.
     * @param url URL completa de la siguiente página
     * @return Flow con estados Resource<CharactersPage>
     */
    suspend operator fun invoke(url: String): Flow<Resource<CharactersPage>> = repository.getCharactersByUrl(url)
}
