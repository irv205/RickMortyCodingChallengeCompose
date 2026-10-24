/**
 * Archivo: AppRepositoryImpl.kt
 * 
 * Propósito:
 * Implementación concreta del repositorio definido en Domain.
 * Orquesta la obtención de datos desde la API, mapea DTOs a modelos Domain.
 * 
 * Responsabilidades:
 * - Llamar a ApiService para obtener datos
 * - Manejar respuestas HTTP (éxito/error)
 * - Usar CharacterMapper para convertir DTO -> Domain
 * - Emitir estados con Resource (Loading, Success, Error)
 * - Capturar excepciones y convertirlas a Resource.Error
 * 
 * @Inject constructor(): Hilt provee ApiService y CharacterMapper automáticamente
 * : AppRepository: Implementa la interfaz definida en Domain
 * 
 * Nota: Podríamos usar safeApiCall() aquí para reducir código repetitivo,
 * pero la implementación actual es explícita y clara para propósitos educativos.
 */
package com.irv205.rickmortycodingchallengecompose.data.repository

import com.irv205.rickmortycodingchallengecompose.core.util.Resource
import com.irv205.rickmortycodingchallengecompose.data.mapper.CharacterMapper
import com.irv205.rickmortycodingchallengecompose.data.service.ApiService
import com.irv205.rickmortycodingchallengecompose.domain.model.Character
import com.irv205.rickmortycodingchallengecompose.domain.model.CharactersPage
import com.irv205.rickmortycodingchallengecompose.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: CharacterMapper
) : AppRepository {
    
    /**
     * Obtiene primera página de personajes desde la API.
     * Emite: Loading -> Success (con datos mapeados) o Error
     */
    override suspend fun getCharacters(): Flow<Resource<CharactersPage>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getCharacters()
            if (response.isSuccessful && response.body() != null) {
                // Mapea DTO a modelo Domain usando el mapper
                val page = mapper.mapToPage(response.body()!!)
                emit(Resource.Success(page))
            } else {
                emit(Resource.Error(response.message() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error", e))
        }
    }

    /**
     * Obtiene página desde URL completa para paginación infinita.
     */
    override suspend fun getCharactersByUrl(url: String): Flow<Resource<CharactersPage>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getCharactersByUrl(url)
            if (response.isSuccessful && response.body() != null) {
                val page = mapper.mapToPage(response.body()!!)
                emit(Resource.Success(page))
            } else {
                emit(Resource.Error(response.message() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error", e))
        }
    }

    /**
     * Obtiene detalle de un personaje individual por ID.
     */
    override suspend fun getCharacterById(id: Int): Flow<Resource<Character>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getCharacterById(id)
            if (response.isSuccessful && response.body() != null) {
                // Mapea DTO individual a modelo Domain
                val character = mapper.mapToDomain(response.body()!!)
                emit(Resource.Success(character))
            } else {
                emit(Resource.Error(response.message() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error", e))
        }
    }
}
