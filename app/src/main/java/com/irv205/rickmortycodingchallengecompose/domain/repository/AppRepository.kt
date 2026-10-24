/**
 * Archivo: AppRepository.kt
 * 
 * Propósito:
 * Contrato/Interfaz del repositorio definido en la capa Domain.
 * 
 * Principio de Inversión de Dependencias (DIP):
 * - Domain define QUÉ se necesita (interfaz)
 * - Data implementa CÓMO se obtiene (implementación)
 * - Esto hace que Domain no dependa de detalles de implementación (Retrofit)
 * 
 * ¿Por qué usar Flow<Resource<...>>?
 * - Flow: Stream reactivo de datos. Se puede observar y reaccionar a cambios
 * - Resource: Envuelve el estado (Loading/Success/Error)
 * - Permite emitir múltiples valores a lo largo del tiempo
 * 
 * UseCases usarán este repositorio, nunca llamarán directamente a ApiService.
 */
package com.irv205.rickmortycodingchallengecompose.domain.repository

import com.irv205.rickmortycodingchallengecompose.core.util.Resource
import com.irv205.rickmortycodingchallengecompose.domain.model.Character
import com.irv205.rickmortycodingchallengecompose.domain.model.CharactersPage
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    /**
     * Obtiene la primera página de personajes.
     * @return Flow que emite estados Resource con CharactersPage
     */
    suspend fun getCharacters(): Flow<Resource<CharactersPage>>
    
    /**
     * Obtiene una página específica usando URL completa (paginación infinita).
     * @param url URL completa de la página a obtener
     * @return Flow que emite estados Resource con CharactersPage
     */
    suspend fun getCharactersByUrl(url: String): Flow<Resource<CharactersPage>>
    
    /**
     * Obtiene el detalle completo de un personaje por ID.
     * @param id ID del personaje
     * @return Flow que emite estados Resource con Character
     */
    suspend fun getCharacterById(id: Int): Flow<Resource<Character>>
}
