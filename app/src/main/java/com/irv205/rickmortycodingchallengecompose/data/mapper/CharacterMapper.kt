/**
 * Archivo: CharacterMapper.kt
 * 
 * Propósito:
 * Transforma objetos DTO (Data layer) a modelos Domain (Domain layer).
 * Actúa como puente entre las capas, manteniendo el desacoplamiento.
 * 
 * ¿Por qué mapear?
 * - DTOs pueden tener campos que no necesitamos en la UI
 * - Simplificamos: en lugar de anidar LocationDTO, extraemos solo originName y locationName
 * - Domain queda limpio, sin dependencias de Moshi/Retrofit
 * - Si cambia la estructura de la API, el impacto se limita a mapper + DTOs
 * 
 * @Inject constructor(): Marcado para que Hilt pueda inyectarlo automáticamente
 */
package com.irv205.rickmortycodingchallengecompose.data.mapper

import com.irv205.rickmortycodingchallengecompose.data.model.CharacterDTO
import com.irv205.rickmortycodingchallengecompose.data.model.CharactersResponseDTO
import com.irv205.rickmortycodingchallengecompose.domain.model.Character
import com.irv205.rickmortycodingchallengecompose.domain.model.CharactersPage
import javax.inject.Inject

class CharacterMapper @Inject constructor() {
    /**
     * Convierte un CharacterDTO individual a modelo Domain Character.
     * Extrae solo los campos necesarios para mostrar en UI.
     * 
     * @param dto Objeto recibido de la API
     * @return Modelo limpio para usar en Domain/Presentation
     */
    fun mapToDomain(dto: CharacterDTO): Character {
        return Character(
            id = dto.id,
            name = dto.name,
            status = dto.status,
            species = dto.species,
            type = dto.type,
            gender = dto.gender,
            originName = dto.origin.name,      // Extrae solo el nombre del objeto origin
            locationName = dto.location.name,   // Extrae solo el nombre del objeto location
            image = dto.image,
            created = dto.created
        )
    }

    /**
     * Convierte la respuesta paginada completa a CharactersPage (Domain).
     * Mapea todos los resultados usando mapToDomain() y extrae la URL de siguiente página.
     * 
     * @param dto Respuesta completa con info y results
     * @return Página de personajes con URL para paginación
     */
    fun mapToPage(dto: CharactersResponseDTO): CharactersPage {
        return CharactersPage(
            characters = dto.results.map { mapToDomain(it) },
            nextPageUrl = dto.info.next
        )
    }
}
