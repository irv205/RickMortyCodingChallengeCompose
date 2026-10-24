/**
 * Archivo: SafeApiCall.kt
 * 
 * Propósito:
 * Función de extensión/utilidad para envolver llamadas a API de Retrofit de forma segura
 * y convertirlas a un Flow<Resource<T>>.
 * 
 * ¿Qué hace?
 * 1. Ejecuta la llamada a la API (suspend function que retorna Response<T>)
 * 2. Si la respuesta es exitosa y tiene body: emite Resource.Success
 * 3. Si la respuesta es exitosa pero body es null: emite Resource.Error("Empty body")
 * 4. Si la respuesta no es exitosa: emite Resource.Error con el mensaje de Retrofit
 * 5. Siempre emite Resource.Loading al inicio (onStart)
 * 6. Captura cualquier excepción (red, timeout, parseo) y emite Resource.Error
 * 
 * Operadores de Flow usados:
 * - flow { }: Crea un Flow personalizado con lógica imperativa
 * - onStart { }: Se ejecuta al inicio de la recolección, ideal para emitir estado Loading
 * - catch { }: Captura excepciones y las transforma en Resource.Error
 * - map { }: Transforma cada elemento del Flow (no usado aquí pero disponible)
 * 
 * Uso: Permite tener un patrón consistente para todas las llamadas de red.
 */
package com.irv205.rickmortycodingchallengecompose.core.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import retrofit2.Response

/**
 * Envuelve una llamada a API de Retrofit en un Flow<Resource<T>> con manejo de estados.
 * 
 * @param T Tipo de dato esperado en la respuesta exitosa
 * @param apiCall Lambda suspend que ejecuta la llamada a Retrofit y retorna Response<T>
 * @return Flow que emite Loading, luego Success o Error
 */
fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Flow<Resource<T>> {
    return flow {
        // Ejecuta la llamada HTTP
        val response = apiCall()
        
        if (response.isSuccessful) {
            // Verifica que el body no sea nulo
            val body = response.body()
            if (body != null) {
                // Éxito: emite los datos mapeados/envueltos en Resource.Success
                emit(Resource.Success(body))
            } else {
                // Respuesta exitosa pero sin cuerpo (poco común en esta API)
                emit(Resource.Error("Empty body"))
            }
        } else {
            // Error HTTP (404, 500, etc.)
            emit(Resource.Error(response.message()))
        }
    }
        // Emite Loading inmediatamente al iniciar la recolección
        .onStart { emit(Resource.Loading) }
        // Captura cualquier excepción durante la ejecución (sin conexión, timeout, etc.)
        .catch { e -> 
            emit(Resource.Error(e.localizedMessage ?: "Unknown error", e)) 
        }
}
