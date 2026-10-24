/**
 * Archivo: ApiService.kt
 * 
 * Propósito:
 * Interfaz que define los endpoints HTTP de la Rick and Morty API usando anotaciones de Retrofit.
 * Retrofit se encarga de generar la implementación en tiempo de compilación.
 * 
 * Endpoints usados:
 * - GET /character -> Lista paginada de personajes
 * - GET /character/{id} -> Detalle de un personaje específico
 * - GET (URL completa) -> Para paginación con la URL "next" que provee la API
 * 
 * Anotaciones:
 * - @GET: Método HTTP GET
 * - @Path: Reemplaza {id} en la URL con el valor pasado
 * - @Url: Permite pasar una URL completa (usado para paginación, ya que "next" viene completo)
 * - suspend: Función coroutine-friendly, no bloquea el thread principal
 * - Response<T>: Envuelve la respuesta con código HTTP, headers, etc. Permite verificar isSuccessful
 * 
 * API Docs: https://rickandmortyapi.com/documentation
 */
package com.irv205.rickmortycodingchallengecompose.data.service

import com.irv205.rickmortycodingchallengecompose.data.model.CharacterDTO
import com.irv205.rickmortycodingchallengecompose.data.model.CharactersResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {
    /**
     * Obtiene la primera página de personajes.
     * @return Response con CharactersResponseDTO que contiene info (paginación) y results (lista)
     */
    @GET("character")
    suspend fun getCharacters(): Response<CharactersResponseDTO>

    /**
     * Obtiene personajes desde una URL completa (usado para paginación).
     * La API devuelve "next" con la URL completa para la siguiente página.
     * @param url URL completa de la siguiente página
     * @return Response con CharactersResponseDTO
     */
    @GET
    suspend fun getCharactersByUrl(@Url url: String): Response<CharactersResponseDTO>

    /**
     * Obtiene el detalle de un personaje por su ID.
     * @param id ID único del personaje
     * @return Response con CharacterDTO (personaje individual)
     */
    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): Response<CharacterDTO>
}
