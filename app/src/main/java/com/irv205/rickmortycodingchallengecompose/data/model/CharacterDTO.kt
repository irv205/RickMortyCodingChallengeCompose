/**
 * Archivo: CharacterDTO.kt
 * 
 * Propósito:
 * Data Transfer Objects (DTOs) que representan la estructura JSON exacta que retorna la API.
 * Estos modelos pertenecen a la capa DATA, NO a Domain.
 * 
 * ¿Por qué DTOs separados del Domain?
 * - Desacoplamiento: Si la API cambia, solo modificamos DTOs y mapper, no toda la app
 * - Clean Architecture: Domain debe ser independiente de frameworks (Retrofit, JSON, etc.)
 * - Control sobre qué campos exponemos al resto de la app
 * 
 * Anotaciones Moshi:
 * - @JsonClass(generateAdapter = true): Genera un JsonAdapter en tiempo de compilación (más rápido)
 * - @Json(name = "..."): Mapea el nombre del campo JSON al nombre de la propiedad Kotlin
 * 
 * Estructura de respuesta de /character:
 * {
 *   "info": { "count": 826, "pages": 42, "next": "...", "prev": null },
 *   "results": [ { ...personaje... }, ... ]
 * }
 */
package com.irv205.rickmortycodingchallengecompose.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO que representa la respuesta paginada de la lista de personajes.
 */
@JsonClass(generateAdapter = true)
data class CharactersResponseDTO(
    @Json(name = "info") val info: InfoDTO,
    @Json(name = "results") val results: List<CharacterDTO>
)

/**
 * DTO con información de paginación proporcionada por la API.
 */
@JsonClass(generateAdapter = true)
data class InfoDTO(
    @Json(name = "count") val count: Int,
    @Json(name = "pages") val pages: Int,
    @Json(name = "next") val next: String?,
    @Json(name = "prev") val prev: String?
)

/**
 * DTO que representa un personaje tal como viene de la API.
 * Contiene todos los campos devueltos por el endpoint.
 */
@JsonClass(generateAdapter = true)
data class CharacterDTO(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "status") val status: String,
    @Json(name = "species") val species: String,
    @Json(name = "type") val type: String,
    @Json(name = "gender") val gender: String,
    @Json(name = "origin") val origin: LocationDTO,
    @Json(name = "location") val location: LocationDTO,
    @Json(name = "image") val image: String,
    @Json(name = "episode") val episode: List<String>,
    @Json(name = "url") val url: String,
    @Json(name = "created") val created: String
)

/**
 * DTO para origen/ubicación de un personaje.
 * La API retorna objetos {name, url} para origin y location.
 */
@JsonClass(generateAdapter = true)
data class LocationDTO(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)
