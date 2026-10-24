/**
 * Archivo: Screen.kt
 * 
 * Propósito:
 * Define las rutas de navegación de la app de forma type-safe usando Kotlin Serialization.
 * 
 * ¿Por qué type-safe navigation?
 * - Evita errores por strings mal escritos en rutas.
 * - Los argumentos se pasan como propiedades del objeto/clase, con tipado fuerte.
 * - Navigation Compose genera el código necesario para serializar/deserializar automáticamente.
 * 
 * @Serializable: Marca las clases/objetos para que Kotlin Serialization pueda convertirlos
 *               a/desde rutas de navegación.
 * 
 * sealed class Screen: Agrupa todas las rutas posibles. Solo las clases dentro de este
 *                       archivo pueden heredar de Screen, lo que hace el sistema exhaustivo.
 */
package com.irv205.rickmortycodingchallengecompose.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    /**
     * Ruta para la pantalla de lista de personajes (pantalla principal).
     * Es un data object porque no necesita parámetros.
     */
    @Serializable
    data object CharacterList : Screen()

    /**
     * Ruta para la pantalla de detalle de un personaje.
     * @param characterId ID único del personaje a mostrar (obtenido de Rick and Morty API)
     */
    @Serializable
    data class CharacterDetail(val characterId: Int) : Screen()
}
