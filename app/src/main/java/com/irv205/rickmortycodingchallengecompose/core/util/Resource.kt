/**
 * Archivo: Resource.kt
 * 
 * Propósito:
 * Clase sellada (sealed class) para representar el estado de una operación asíncrona
 * (carga de datos desde red/base de datos).
 * 
 * Patrón Resource/Wrapper:
 * - Permite al UI reaccionar correctamente según el estado: mostrar loading, datos o error.
 * - Centraliza el manejo de estados en lugar de usar múltiples booleanos (isLoading, hasError, etc.)
 * 
 * Estados:
 * - Loading: Operación en progreso - mostrar indicador de carga
 * - Success<T>: Operación exitosa con datos de tipo T
 * - Error: Operación fallida con mensaje y opcionalmente el throwable para debugging
 * 
 * Ventajas:
 * - Type-safe: El compilador obliga a manejar todos los estados con when{}
 * - Reutilizable en toda la app
 * - Facilita el testing
 */
package com.irv205.rickmortycodingchallengecompose.core.util

/**
 * Wrapper genérico para estados de operaciones asíncronas.
 * @param T Tipo de dato esperado en caso de éxito
 */
sealed class Resource<out T> {
    /**
     * Estado de éxito: la operación terminó correctamente.
     * @param data Los datos obtenidos
     */
    data class Success<T>(val data: T) : Resource<T>()
    
    /**
     * Estado de error: la operación falló.
     * @param message Mensaje descriptivo del error para mostrar al usuario
     * @param throwable Excepción original para logs/debugging (opcional)
     */
    data class Error(val message: String, val throwable: Throwable? = null) : Resource<Nothing>()
    
    /**
     * Estado de carga: la operación está en progreso.
     * data object porque no tiene datos/estado interno
     */
    data object Loading : Resource<Nothing>()
}
