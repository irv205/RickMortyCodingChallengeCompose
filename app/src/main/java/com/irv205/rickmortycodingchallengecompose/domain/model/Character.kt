/**
 * Archivo: Character.kt
 * 
 * Propósito:
 * Modelos de dominio (Domain Models) - representan las entidades de negocio.
 * Esta capa es la más independiente: no tiene dependencias Android, Retrofit, ni Compose.
 * 
 * Clean Architecture - Domain Layer:
 * - Contiene reglas de negocio y entidades
 * - No conoce Data ni Presentation
 * - Solo Kotlin puro
 * - Puede ser reutilizado por otras plataformas
 * 
 * Diferencia con DTO:
 * - DTO: Estructura exacta de la API (puede tener campos anidados complejos)
 * - Domain: Estructura optimizada para la app (simplificada, lo que realmente usamos)
 */
package com.irv205.rickmortycodingchallengecompose.domain.model

/**
 * Entidad de dominio que representa un personaje de Rick and Morty.
 * Modelo simplificado vs DTO - solo contiene lo necesario para la UI.
 * 
 * @param id Identificador único
 * @param name Nombre del personaje
 * @param status Estado: "Alive", "Dead", o "unknown"
 * @param species Especie: "Human", "Alien", etc.
 * @param type Tipo/subespecie (puede estar vacío)
 * @param gender Género: "Male", "Female", "Genderless", "unknown"
 * @param originName Nombre del planeta/origen (simplificado desde LocationDTO)
 * @param locationName Nombre de la ubicación actual (simplificado desde LocationDTO)
 * @param image URL de la imagen del personaje
 * @param created Fecha de creación en la API
 */
data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val originName: String,
    val locationName: String,
    val image: String,
    val created: String
)

/**
 * Representa una página de personajes con información para paginación infinita.
 * 
 * @param characters Lista de personajes en esta página
 * @param nextPageUrl URL de la siguiente página, null si es la última página
 */
data class CharactersPage(
    val characters: List<Character>,
    val nextPageUrl: String?
)
